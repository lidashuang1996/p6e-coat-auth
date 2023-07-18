package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.Oauth2CodeCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.gateway.auth.certificate.HttpCertificate;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.*;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 TOKEN 的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenServiceImpl implements Oauth2TokenService {

    /**
     * 密码模式
     */
    private static final String PASSWORD_TYPE = "password";

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
     * 用户存储库
     */
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    /**
     * OAUTH2 客户端存储库
     */
    private final Oauth2ClientRepository oauth2ClientRepository;

    private final AuthUser<?> au;

    /**
     * @param properties             配置文件对象
     * @param userRepository         用户存储库
     * @param oauth2ClientRepository OAUTH2 客户端存储库
     */
    public Oauth2TokenServiceImpl(
            AuthUser<?> au,
            Properties properties,
            UserRepository userRepository,
            UserAuthRepository userAuthRepository,
            Oauth2ClientRepository oauth2ClientRepository) {
        this.au = au;
        this.properties = properties;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
        this.oauth2ClientRepository = oauth2ClientRepository;
    }

    @Override
    public Mono<Oauth2Context.Token.Dto> execute(ServerWebExchange exchange, Oauth2Context.Token.Request param) {
        final String grantType = param.getGrantType();
        switch (grantType) {
            case PASSWORD_TYPE -> {
                if (properties.getOauth2().getPassword().isEnable()) {
                    return executePasswordType(param);
                } else {
                    return Mono.error(
                            GlobalExceptionContext.executeServiceNotEnabledException(
                                    this.getClass(),
                                    "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                    "Oauth2 token [ PASSWORD ] service not enabled exception."
                            ));
                }
            }
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
    private Mono<Oauth2Context.Token.Dto> executeClientType(Oauth2Context.Token.Request param) {
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
                                "Oauth2 [ " + Oauth2TokenClientAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    if (!SpringUtil.exist(Oauth2TokenClientAuthAccessTokenGenerator.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun handleUserResult(String cid, String uid, String info, String scope)",
                                "Oauth2 [ " + Oauth2TokenClientAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    if (!SpringUtil.exist(Oauth2TokenClientAuthRefreshTokenGenerator.class)) {
                        return Mono.error(GlobalExceptionContext.exceptionBeanException(
                                this.getClass(),
                                "fun handleUserResult(String cid, String uid, String info, String scope)",
                                "Oauth2 [ " + Oauth2TokenClientAuthRefreshTokenGenerator.class + " ] handle bean not exist exception."
                        ));
                    }
                    final String accessToken = SpringUtil.getBean(
                            Oauth2TokenClientAuthAccessTokenGenerator.class).execute();
                    final String refreshToken = SpringUtil.getBean(
                            Oauth2TokenClientAuthRefreshTokenGenerator.class).execute();
                    final Oauth2TokenClientAuthCache oauth2TokenClientAuthCache = SpringUtil.getBean(Oauth2TokenClientAuthCache.class);
                    return oauth2TokenClientAuthCache
                            .set(String.valueOf(m.getId()), JsonUtil.toJson(m), m.getScope(), accessToken, refreshToken)
                            .map(t -> new Oauth2Context.Token.ClientDto()
                                    .setId(String.valueOf(t.getCid()))
                                    .setAccessToken(accessToken)
                                    .setRefreshToken(refreshToken)
                                    .setType(HttpCertificate.getAuthHeaderTokenType())
                                    .setExpiration(Oauth2TokenUserAuthCache.EXPIRATION_TIME));
                });
    }

    /**
     * Oauth2 密码模式
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<Oauth2Context.Token.Dto> executePasswordType(Oauth2Context.Token.Request param) {
        final String username = param.getUsername();
        final String password = param.getPassword();
        final String clientId = param.getClientId();
        final String clientSecret = param.getClientSecret();
        return oauth2ClientRepository
                .findByClientId(clientId)
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                        this.getClass(),
                        "fun executePasswordType(Oauth2Context.Token.Request param)",
                        "Oauth2 client id not exist exception."
                )))
                .flatMap(m -> {
                    if (!m.getClientSecret().equals(clientSecret)) {
                        return Mono.error(GlobalExceptionContext.executeOauth2ParameterException(
                                this.getClass(),
                                "fun executePasswordType(Oauth2Context.Token.Request param)",
                                "Oauth2 client secret exception."
                        ));
                    }
                    final Properties.Mode mode = properties.getMode();
                    return (switch (mode) {
                        case PHONE -> userRepository.findByPhone(username);
                        case MAILBOX -> userRepository.findByMailbox(username);
                        case ACCOUNT -> userRepository.findByAccount(username);
                        case PHONE_OR_MAILBOX -> userRepository.findByPhoneOrMailbox(username);
                    })
                            .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAccountPasswordLoginAccountOrPasswordException(
                                    this.getClass(),
                                    "fun executePasswordType(Oauth2Context.Token.Request param)",
                                    "Oauth2 client account/password exception."
                            )))
                            .flatMap(u -> userAuthRepository
                                    .findById(u.getId())
                                    .filter(uam -> {
                                        if (SpringUtil.exist(AuthPasswordEncryptor.class)) {
                                            final AuthPasswordEncryptor passwordEncryptor = SpringUtil.getBean(AuthPasswordEncryptor.class);
                                            return uam.getPassword().equals(passwordEncryptor.execute(password));
                                        } else {
                                            return false;
                                        }
                                    })
                                    .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAccountPasswordLoginAccountOrPasswordException(
                                            this.getClass(),
                                            "fun executePasswordType(Oauth2Context.Token.Request param)",
                                            "Oauth2 client account/password exception."
                                    )))
                                    .flatMap(uam -> handleUserResult(
                                            m.getClientId(),
                                            String.valueOf(uam.getId()),
                                            au.create(u, uam).serialize(),
                                            m.getScope()
                                    )));
                });

    }

    /**
     * Oauth2 CODE 模式
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<Oauth2Context.Token.Dto> executeAuthorizationType(Oauth2Context.Token.Request param) {
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
    private Mono<Oauth2Context.Token.Dto> handleUserResult(String cid, String uid, String info, String scope) {
        if (!SpringUtil.exist(Oauth2TokenUserAuthAccessTokenGenerator.class)) {
            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                    this.getClass(),
                    "fun handleUserResult(String cid, String uid, String info, String scope)",
                    "Oauth2 [ " + Oauth2TokenUserAuthAccessTokenGenerator.class + " ] handle bean not exist exception."
            ));
        }
        if (!SpringUtil.exist(Oauth2TokenUserAuthRefreshTokenGenerator.class)) {
            return Mono.error(GlobalExceptionContext.exceptionBeanException(
                    this.getClass(),
                    "fun handleUserResult(String cid, String uid, String info, String scope)",
                    "Oauth2 [ " + Oauth2TokenUserAuthRefreshTokenGenerator.class + " ] handle bean not exist exception."
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
                Oauth2TokenUserAuthAccessTokenGenerator.class).execute();
        final String refreshToken = SpringUtil.getBean(
                Oauth2TokenUserAuthRefreshTokenGenerator.class).execute();
        final Oauth2TokenUserAuthCache oauth2TokenUserAuthCache =
                SpringUtil.getBean(Oauth2TokenUserAuthCache.class);
        final Oauth2UserOpenIdGenerator oauth2UserOpenIdGenerator =
                SpringUtil.getBean(Oauth2UserOpenIdGenerator.class);
        final String openid = oauth2UserOpenIdGenerator.execute(cid, uid);
        return oauth2TokenUserAuthCache
                .set(uid, info, scope, accessToken, refreshToken)
                .map(t -> new Oauth2Context.Token.UserDto()
                        .setOpenId(openid)
                        .setAccessToken(t.getAccessToken())
                        .setRefreshToken(t.getRefreshToken())
                        .setType(HttpCertificate.getAuthHeaderTokenType())
                        .setExpiration(Oauth2TokenUserAuthCache.EXPIRATION_TIME)
                );
    }

}
