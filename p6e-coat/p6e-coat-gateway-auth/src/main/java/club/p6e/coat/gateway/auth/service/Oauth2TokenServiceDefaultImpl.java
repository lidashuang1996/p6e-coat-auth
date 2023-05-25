package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.Oauth2CodeCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.*;
import club.p6e.coat.gateway.auth.model.UserModel;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * OAUTH2 TOKEN 的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = Oauth2TokenService.class,
        ignored = Oauth2TokenServiceDefaultImpl.class
)
@ConditionalOnExpression(Oauth2TokenService.CONDITIONAL_EXPRESSION)
public class Oauth2TokenServiceDefaultImpl implements Oauth2TokenService {

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
     * OAUTH2 CODE 缓存
     */
    private final Oauth2CodeCache oauth2CodeCache;

    /**
     * OAUTH2 用户认证缓存
     */
    private final Oauth2TokenUserAuthCache oauth2TokenUserAuthCache;

    /**
     * OAUTH2 客户端认证缓存
     */
    private final Oauth2TokenClientAuthCache oauth2TokenClientAuthCache;

    /**
     * 用户存储库
     */
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    /**
     * OAUTH2 客户端存储库
     */
    private final Oauth2ClientRepository oauth2ClientRepository;

    /**
     * OAUTH2 USER OPEN ID 生成器
     */
    private final Oauth2UserOpenIdGenerator oauth2UserOpenIdGenerator;

    /**
     * 令牌生成器
     */
    private final Oauth2TokenUserAuthAccessTokenGenerator oauth2TokenUserAuthAccessTokenGenerator;

    /**
     * 令牌生成器
     */
    private final Oauth2TokenUserAuthRefreshTokenGenerator oauth2TokenUserAuthRefreshTokenGenerator;

    /**
     * 令牌生成器
     */
    private final Oauth2TokenClientAuthAccessTokenGenerator oauth2TokenClientAuthAccessTokenGenerator;

    /**
     * 令牌生成器
     */
    private final Oauth2TokenClientAuthRefreshTokenGenerator oauth2TokenClientAuthRefreshTokenGenerator;

    private final AuthPasswordEncryptor passwordEncryptor;

    /**
     * @param properties                 配置文件对象
     * @param oauth2CodeCache            OAUTH2 CODE 缓存
     * @param oauth2TokenUserAuthCache   OAUTH2 用户认证缓存
     * @param oauth2TokenClientAuthCache OAUTH2 客户端认证缓存
     * @param userRepository             用户存储库
     * @param oauth2ClientRepository     OAUTH2 客户端存储库
     * @param oauth2UserOpenIdGenerator  OAUTH2 USER OPEN ID 生成器
     */
    public Oauth2TokenServiceDefaultImpl(
            Properties properties,
            Oauth2CodeCache oauth2CodeCache,
            Oauth2TokenUserAuthCache oauth2TokenUserAuthCache,
            Oauth2TokenClientAuthCache oauth2TokenClientAuthCache,
            UserAuthRepository userAuthRepository,
            UserRepository userRepository,
            AuthPasswordEncryptor passwordEncryptor,
            Oauth2ClientRepository oauth2ClientRepository,
            Oauth2UserOpenIdGenerator oauth2UserOpenIdGenerator,
            Oauth2TokenUserAuthAccessTokenGenerator oauth2TokenUserAuthAccessTokenGenerator,
            Oauth2TokenUserAuthRefreshTokenGenerator oauth2TokenUserAuthRefreshTokenGenerator,
            Oauth2TokenClientAuthAccessTokenGenerator oauth2TokenClientAuthAccessTokenGenerator,
            Oauth2TokenClientAuthRefreshTokenGenerator oauth2TokenClientAuthRefreshTokenGenerator) {
        this.properties = properties;
        this.passwordEncryptor = passwordEncryptor;
        this.userAuthRepository = userAuthRepository;
        this.oauth2CodeCache = oauth2CodeCache;
        this.oauth2TokenUserAuthCache = oauth2TokenUserAuthCache;
        this.oauth2TokenClientAuthCache = oauth2TokenClientAuthCache;
        this.userRepository = userRepository;
        this.oauth2ClientRepository = oauth2ClientRepository;
        this.oauth2UserOpenIdGenerator = oauth2UserOpenIdGenerator;
        this.oauth2TokenUserAuthAccessTokenGenerator = oauth2TokenUserAuthAccessTokenGenerator;
        this.oauth2TokenUserAuthRefreshTokenGenerator = oauth2TokenUserAuthRefreshTokenGenerator;
        this.oauth2TokenClientAuthAccessTokenGenerator = oauth2TokenClientAuthAccessTokenGenerator;
        this.oauth2TokenClientAuthRefreshTokenGenerator = oauth2TokenClientAuthRefreshTokenGenerator;
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
                .findOneByClientId(clientId)
                .switchIfEmpty(Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(m -> {
                    if (!m.getClientSecret().equals(clientSecret)) {
                        return Mono.error(
                                GlobalExceptionContext.executeServiceNotEnabledException(
                                        this.getClass(),
                                        "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                        "Account password login service not enabled exception."
                                ));
                    }
                    final Properties.Mode mode = properties.getMode();
                    return (switch (mode) {
                        case PHONE -> userRepository.findOneByPhone(username);
                        case MAILBOX -> userRepository.findOneByMailbox(username);
                        case ACCOUNT -> userRepository.findOneByAccount(username);
                        case PHONE_OR_MAILBOX -> userRepository.findOneByPhoneOrMailbox(username);
                    }).switchIfEmpty(Mono.error(
                                    GlobalExceptionContext.executeServiceNotEnabledException(
                                            this.getClass(), "fun executePasswordType(Oauth2Context.Token.Request param).",
                                            "Account password login service not enabled exception."
                                    )))
                            .flatMap(u -> userAuthRepository
                                    .findOneById(u.getId())
                                    .switchIfEmpty(Mono.error(
                                            GlobalExceptionContext.executeServiceNotEnabledException(
                                                    this.getClass(), "fun executePasswordType(Oauth2Context.Token.Request param).",
                                                    "Account password login service not enabled exception."
                                            )))
                                    .filter(au -> au.getPassword().equals(passwordEncryptor.execute(password)))
                                    .switchIfEmpty(Mono.error(
                                            GlobalExceptionContext.executeServiceNotEnabledException(
                                                    this.getClass(), "fun executePasswordType(Oauth2Context.Token.Request param).",
                                                    "Account password login service not enabled exception."
                                            )))
                                    .flatMap(au -> {
                                        final String accessToken = oauth2TokenUserAuthAccessTokenGenerator.execute();
                                        final String refreshToken = oauth2TokenUserAuthRefreshTokenGenerator.execute();
                                        final String openId = oauth2UserOpenIdGenerator.execute(clientId, String.valueOf(u.getId()));
                                        return oauth2TokenUserAuthCache.set(
                                                String.valueOf(u.getId()),
                                                JsonUtil.toJson(u),
                                                m.getScope(),
                                                accessToken,
                                                refreshToken
                                        ).map(t -> new Oauth2Context.Token.UserDto()
                                                .setOpenId(openId)
                                                .setAccessToken(t.getAccessToken())
                                                .setRefreshToken(t.getRefreshToken())
                                                .setExpiration(Oauth2TokenUserAuthCache.EXPIRATION_TIME));
                                    }));
                });

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
                .findOneByClientId(clientId)
                .switchIfEmpty(Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(m -> {
                    if (!m.getClientSecret().equals(clientSecret)) {
                        return Mono.error(
                                GlobalExceptionContext.executeServiceNotEnabledException(
                                        this.getClass(),
                                        "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                        "Account password login service not enabled exception."
                                ));
                    }
                    final String accessToken = oauth2TokenClientAuthAccessTokenGenerator.execute();
                    final String refreshToken = oauth2TokenClientAuthRefreshTokenGenerator.execute();
                    return oauth2TokenClientAuthCache
                            .set(String.valueOf(m.getId()), JsonUtil.toJson(m), m.getScope(), accessToken, refreshToken)
                            .map(t -> new Oauth2Context.Token.ClientDto()
                                    .setId(String.valueOf(t.getCid()))
                                    .setAccessToken(t.getAccessToken())
                                    .setRefreshToken(t.getRefreshToken())
                                    .setExpiration(Oauth2TokenUserAuthCache.EXPIRATION_TIME));
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
                .findOneByClientId(clientId)
                .switchIfEmpty(Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(m -> oauth2CodeCache
                        .get(code)
                        .switchIfEmpty(Mono.error(
                                GlobalExceptionContext.executeServiceNotEnabledException(
                                        this.getClass(),
                                        "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                        "Account password login service not enabled exception."
                                )))
                        .flatMap(map -> {
                            final String codeContentScope = map.get(Oauth2CodeCache.OAUTH2_SCOPE);
                            final String codeContentUserId = map.get(Oauth2CodeCache.OAUTH2_USER_ID);
                            final String codeContentUserInfo = map.get(Oauth2CodeCache.OAUTH2_USER_INFO);
                            final String codeContentClientId = map.get(Oauth2CodeCache.OAUTH2_CLIENT_ID);
                            final String codeContentRedirectUri = map.get(Oauth2CodeCache.OAUTH2_REDIRECT_URI);
                            if (!m.getClientSecret().equals(clientSecret)) {
                                return Mono.error(
                                        GlobalExceptionContext.executeServiceNotEnabledException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                                "Account password login service not enabled exception."
                                        ));
                            }
                            if (!codeContentClientId.equals(clientId)) {
                                return Mono.error(
                                        GlobalExceptionContext.executeServiceNotEnabledException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                                "Account password login service not enabled exception."
                                        ));
                            }
                            if (!codeContentRedirectUri.equals(redirectUri)) {
                                return Mono.error(
                                        GlobalExceptionContext.executeServiceNotEnabledException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                                "Account password login service not enabled exception."
                                        ));
                            }
                            final String accessToken = oauth2TokenUserAuthAccessTokenGenerator.execute();
                            final String refreshToken = oauth2TokenUserAuthRefreshTokenGenerator.execute();
                            return oauth2TokenUserAuthCache
                                    .set(codeContentUserId, codeContentUserInfo,
                                            codeContentScope, accessToken, refreshToken)
                                    .map(t -> new Oauth2Context.Token.UserDto()
                                            .setOpenId(oauth2UserOpenIdGenerator.execute(m.getClientId(), codeContentUserId))
                                            .setAccessToken(t.getAccessToken())
                                            .setRefreshToken(t.getRefreshToken())
                                            .setExpiration(Oauth2TokenUserAuthCache.EXPIRATION_TIME));
                        })
                );
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
                                    "Account password login service not enabled exception."
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
                                    "Account password login service not enabled exception."
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
                                    "Account password login service not enabled exception."
                            ));
                }
            }
            default -> {
                return Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        ));
            }
        }
    }
}
