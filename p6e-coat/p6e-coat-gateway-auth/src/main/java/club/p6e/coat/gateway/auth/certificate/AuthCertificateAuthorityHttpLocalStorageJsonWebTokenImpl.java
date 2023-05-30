package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateAuthority;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateAuthorityHttpLocalStorageJsonWebTokenImpl
        extends AuthCertificateInterceptorBaseHttp implements AuthCertificateAuthority {

    protected static final long EXPIRATION_TIME = 3600;

    /**
     * JWT 内容
     */
    private static final String CONTENT = "content";

    /**
     * 认证缓存的对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param jwt JWT 对象
     */
    public AuthCertificateAuthorityHttpLocalStorageJsonWebTokenImpl(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        final Date date = new Date(LocalDateTime.now()
                .plusSeconds(EXPIRATION_TIME).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        final String accessToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(user.id())
                .withClaim(CONTENT, JsonUtil.toJson(user.toMap()))
                // 使用HMAC算法
                .sign(Algorithm.HMAC256(cipher.getAccessTokenSecret()));
        final String refreshToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(user.id())
                .withClaim(CONTENT, JsonUtil.toJson(user.toMap()))
                // 使用HMAC算法
                .sign(Algorithm.HMAC256(cipher.getRefreshTokenSecret()));
        return setHttpCookieToken(exchange.getResponse(), accessToken, refreshToken)
                .flatMap(b -> {
                    return Mono.just("");
                });
    }

}
