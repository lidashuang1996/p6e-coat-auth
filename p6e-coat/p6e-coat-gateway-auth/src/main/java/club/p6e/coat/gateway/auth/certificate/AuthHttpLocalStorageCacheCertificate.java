package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthHttpLocalStorageCacheCertificate
        extends AuthCertificateAbstract implements AuthCertificate {

    /**
     * 令牌过期时间
     * 令牌过期时间默认设置为 3600 秒
     */
    protected static int EXPIRATION_TIME = 10800;

    /**
     * 认证头类型
     */
    protected static final String AUTH_HEADER_TOKEN_TYPE = "Bearer";

    protected final AuthCache cache;
    protected final AuthAccessTokenGenerator accessTokenGenerator;
    protected final AuthRefreshTokenGenerator refreshTokenGenerator;

    public AuthHttpLocalStorageCacheCertificate(
            AuthCache cache, AuthAccessTokenGenerator accessTokenGenerator, AuthRefreshTokenGenerator refreshTokenGenerator) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public Mono<Object> use(ServerWebExchange exchange, AuthUserDetails user) {
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        return cache
                .set(String.valueOf(user.getId()), "PC",
                        accessToken, refreshToken, JsonUtil.toJson(user.toMap()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext
                        .executeCacheException(
                                this.getClass(),
                                "fun use(ServerWebExchange exchange, AuthUserDetails user)",
                                "Authentication cache write exception."
                        )))
                .map(t -> {
                    final Map<String, Object> r = new HashMap<>();
                    r.put("accessToken", accessToken);
                    r.put("refreshToken", refreshToken);
                    r.put("expiration", EXPIRATION_TIME);
                    r.put("type", AUTH_HEADER_TOKEN_TYPE);
                    return r;
                });
    }

}
