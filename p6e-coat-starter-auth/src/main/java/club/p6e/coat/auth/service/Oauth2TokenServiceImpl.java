package club.p6e.coat.auth.service;

import club.p6e.coat.auth.cache.Oauth2CodeCache;
import club.p6e.coat.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.auth.certificate.HttpCertificate;
import club.p6e.coat.auth.generator.*;
import club.p6e.coat.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.auth.utils.JsonUtil;
import club.p6e.coat.auth.utils.SpringUtil;
import club.p6e.coat.auth.AuthUser;
import club.p6e.coat.auth.Properties;
import club.p6e.coat.auth.context.OAuth2Context;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * OAUTH2 TOKEN 的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenServiceImpl implements Oauth2TokenService {

    /**
     * 认证模式
     */
    private static final String AUTHORIZATION_CODE_TYPE = "authorization_code";

    /**
     * 客户端模式
     */
    private static final String CLIENT_CREDENTIALS_TYPE = "client_credentials";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH2 客户端存储库
     */
    private final Oauth2ClientRepository oauth2ClientRepository;

    private final AuthUser<?> au;

    /**
     * @param properties             配置文件对象
     * @param oauth2ClientRepository OAUTH2 客户端存储库
     */
    public Oauth2TokenServiceImpl(
            AuthUser<?> au,
            Properties properties,
            Oauth2ClientRepository oauth2ClientRepository) {
        this.au = au;
        this.properties = properties;
        this.oauth2ClientRepository = oauth2ClientRepository;
    }

    @Override
    public Mono<OAuth2Context.Token.Dto> execute(ServerWebExchange exchange, OAuth2Context.Token.Request param) {
        final String grantType = param.getGrantType();
        switch (grantType) {
            case CLIENT_CREDENTIALS_TYPE -> {
                if (properties.getOauth2().getClient().isEnable()) {
                    return executeClientType(param);
                } else {
                    return Mono.error(
                            GlobalExceptionContext.executeServiceNotEnabledException(
                                    this.getClass(),
                                    "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                    "Oauth2 token [ CLIENT_CREDENTIALS ] service not enabled exception."
                            ));
                }
            }
            case AUTHORIZATION_CODE_TYPE -> {
                if (properties.getOauth2().getAuthorizationCode().isEnable()) {
                    return executeAuthorizationType(param);
                } else {
                    return Mono.error(
                            GlobalExceptionContext.executeServiceNotEnabledException(
                                    this.getClass(),
                                    "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                    "Oauth2 token [ AUTHORIZATION_CODE ] service not enabled exception."
                            ));
                }
            }
            default -> {
                return Mono.error(
                        GlobalExceptionContext.executeServiceNotSupportException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Oauth2 token [ " + grantType + " ] service not support exception."
                        ));
            }
        }
    }

    /**
     * Oauth2 客户端模式
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<OAuth2Context.Token.Dto> executeClientType(OAuth2Context.Token.Request param) {
        final String clientId = param.getClientId();
        final String clientSecret = param.getClientId();
        return oauth2ClientRepository
                .findByClientId(clientId)
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                        this.getClass(),
                        "fun executeClientType(Oauth2Context.Token.Request param)",
                        "Oauth2 client id not exist exception."
                )))
                .flatMap(m -> {
                    if (!m.getClientSecret().equals(clientSecret)) {
                        return Mono.error(GlobalExceptionContext.executeOauth2ParameterException(
                                this.getClass(),
                                "fun executeClientType(Oauth2Context.Token.Request param)",
                                "Oauth2 client secret exception."
                        ));
                    }
                    if (!SpringUtil.exist(Oauth2TokenClientAuthCache.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun handleUserResult(String cid, String uid, String info, String scope)",
                                "Oauth2 [ " + OAuth2TokenClientAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    if (!SpringUtil.exist(OAuth2TokenClientAuthAccessTokenGenerator.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun handleUserResult(String cid, String uid, String info, String scope)",
                                "Oauth2 [ " + OAuth2TokenClientAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    if (!SpringUtil.exist(OAuth2TokenClientAuthRefreshTokenGenerator.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun handleUserResult(String cid, String uid, String info, String scope)",
                                "Oauth2 [ " + OAuth2TokenClientAuthRefreshTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    final String accessToken = SpringUtil.getBean(
                            OAuth2TokenClientAuthAccessTokenGenerator.class).execute();
                    final String refreshToken = SpringUtil.getBean(
                            OAuth2TokenClientAuthRefreshTokenGenerator.class).execute();
                    final Oauth2TokenClientAuthCache oauth2TokenClientAuthCache = SpringUtil.getBean(Oauth2TokenClientAuthCache.class);
                    return oauth2TokenClientAuthCache
                            .set(String.valueOf(m.getId()), JsonUtil.toJson(m), m.getScope(), accessToken, refreshToken)
                            .map(t -> new OAuth2Context.Token.ClientDto()
                                    .setId(String.valueOf(t.getCid()))
                                    .setAccessToken(accessToken)
                                    .setRefreshToken(refreshToken)
                                    .setType(HttpCertificate.getAuthHeaderTokenType())
                                    .setExpire(Oauth2TokenClientAuthCache.EXPIRATION_TIME));
                });
    }

    /**
     * Oauth2 CODE 模式
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<OAuth2Context.Token.Dto> executeAuthorizationType(OAuth2Context.Token.Request param) {
        final String code = param.getCode();
        final String clientId = param.getClientId();
        final String redirectUri = param.getRedirectUri();
        final String clientSecret = param.getClientSecret();
        return oauth2ClientRepository
                .findByClientId(clientId)
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                        this.getClass(),
                        "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                        "Oauth2 client id not exist exception."
                )))
                .flatMap(m -> {
                    if (!SpringUtil.exist(Oauth2CodeCache.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                                "Oauth2 [ " + Oauth2CodeCache.class + " ] handle bean not exist exception."
                        ));
                    }
                    final Oauth2CodeCache oauth2CodeCache = SpringUtil.getBean(Oauth2CodeCache.class);
                    return oauth2CodeCache
                            .get(code)
                            .switchIfEmpty(Mono.error(GlobalExceptionContext.executeOauth2ParameterException(
                                    this.getClass(),
                                    "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                                    "Oauth2 parameter code exception."
                            )))
                            .flatMap(map -> {
                                final String codeContentScope = map.get(Oauth2CodeCache.OAUTH2_SCOPE);
                                final String codeContentUserId = map.get(Oauth2CodeCache.OAUTH2_USER_ID);
                                final String codeContentUserInfo = map.get(Oauth2CodeCache.OAUTH2_USER_INFO);
                                final String codeContentClientId = map.get(Oauth2CodeCache.OAUTH2_CLIENT_ID);
                                final String codeContentRedirectUri = map.get(Oauth2CodeCache.OAUTH2_REDIRECT_URI);
                                if (!m.getClientSecret().equals(clientSecret)) {
                                    return Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                                            this.getClass(),
                                            "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                                            "Oauth2 parameter clientSecret exception."
                                    ));
                                }
                                if (!codeContentClientId.equals(clientId)) {
                                    return Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                                            this.getClass(),
                                            "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                                            "Oauth2 parameter clientId exception."
                                    ));
                                }
                                if (!codeContentRedirectUri.equals(redirectUri)) {
                                    return Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                                            this.getClass(),
                                            "fun executeAuthorizationType(Oauth2Context.Token.Request param)",
                                            "Oauth2 parameter redirectUri exception."
                                    ));
                                }
                                return handleUserResult(clientId, codeContentUserId, codeContentUserInfo, codeContentScope);
                            });
                });
    }

    /**
     * 处理用户的结果返回
     *
     * @param cid   客户端 ID
     * @param uid   用户 ID
     * @param info  用户信息
     * @param scope 作用域
     * @return 结果对象
     */
    private Mono<OAuth2Context.Token.Dto> handleUserResult(String cid, String uid, String info, String scope) {
        if (!SpringUtil.exist(OAuth2TokenUserAuthAccessTokenGenerator.class)) {
            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                    this.getClass(),
                    "fun handleUserResult(String cid, String uid, String info, String scope)",
                    "Oauth2 [ " + OAuth2TokenUserAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
            ));
        }
        if (!SpringUtil.exist(OAuth2TokenUserAuthRefreshTokenGenerator.class)) {
            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                    this.getClass(),
                    "fun handleUserResult(String cid, String uid, String info, String scope)",
                    "Oauth2 [ " + OAuth2TokenUserAuthRefreshTokenGenerator.class + " ] handle bean not exist exception."
            ));
        }
        if (!SpringUtil.exist(Oauth2TokenUserAuthCache.class)) {
            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                    this.getClass(),
                    "fun handleUserResult(String cid, String uid, String info, String scope)",
                    "Oauth2 [ " + Oauth2TokenUserAuthCache.class + " ] handle bean not exist exception."
            ));
        }
        final String accessToken = SpringUtil.getBean(
                OAuth2TokenUserAuthAccessTokenGenerator.class).execute();
        final String refreshToken = SpringUtil.getBean(
                OAuth2TokenUserAuthRefreshTokenGenerator.class).execute();
        final Oauth2TokenUserAuthCache oauth2TokenUserAuthCache =
                SpringUtil.getBean(Oauth2TokenUserAuthCache.class);
        final OAuth2UserOpenIdGenerator oauth2UserOpenIdGenerator =
                SpringUtil.getBean(OAuth2UserOpenIdGenerator.class);
        final String openid = oauth2UserOpenIdGenerator.execute(cid, uid);
        final Map<String, Object> map = au.create(info).toMap();
        map.remove("id");
        final Date date = Date.from(LocalDateTime.now()
                .plusSeconds(Oauth2TokenUserAuthCache.EXPIRATION_TIME)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
        return oauth2TokenUserAuthCache
                .set(uid, info, scope, accessToken, refreshToken)
                .map(t -> new OAuth2Context.Token.UserDto()
                        .setOpenId(openid)
                        .setAccessToken(t.getAccessToken())
                        .setRefreshToken(t.getRefreshToken())
                        .setType(HttpCertificate.getAuthHeaderTokenType())
                        .setExpire(Oauth2TokenUserAuthCache.EXPIRATION_TIME)
                        .setIdToken(JWT
                                .create()
                                .withAudience(uid)
                                .withExpiresAt(date)
                                .withSubject(JsonUtil.toJson(map))
                                .sign(Algorithm.HMAC256(ID_TOKEN_SECRET)))
                );
    }

}
