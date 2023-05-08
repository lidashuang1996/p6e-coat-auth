package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.AuthForeignMinistry;
import club.p6e.coat.gateway.auth.AuthForeignMinistryVisaTemplate;
import club.p6e.coat.gateway.auth.JsonWebTokenCipher;
import club.p6e.coat.gateway.auth.error.AuthException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 外交部
 * 通过 HTTP -> HEADER 的实现
 * 本类采用的是 JWT 的方式进行实现
 * 本类采用的前端缓存方式为 HEADER/LOCALSTORAGE 进行缓存数据
 *
 * @author lidashuang
 * @version 1.0
 */
public class ReactiveHttpHeaderJsonWebTokenCacheAuthForeignMinistryImpl
        extends ReactiveHttpHeaderAuthForeignMinistry implements AuthForeignMinistry {

    /**
     * JWT 内容
     */
    private static final String CONTENT = "content";

    /**
     * JWT 密钥
     */
    private final JsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密钥
     */
    public ReactiveHttpHeaderJsonWebTokenCacheAuthForeignMinistryImpl(JsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationAccessToken(ServerHttpRequest request) {
        final String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                final DecodedJWT decoded = JWT.require(
                        Algorithm.HMAC256(cipher.getAccessTokenSecret())).build().verify(accessToken.trim());
                return Mono.just(AuthForeignMinistryVisaTemplate.deserialization(decoded.getClaim(CONTENT).asString()));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationRefreshToken(ServerHttpRequest request) {
        final String refreshToken = getRefreshToken(request);
        if (refreshToken != null) {
            try {
                final DecodedJWT decoded = JWT.require(
                        Algorithm.HMAC256(cipher.getRefreshTokenSecret())).build().verify(refreshToken.trim());
                return Mono.just(AuthForeignMinistryVisaTemplate.deserialization(decoded.getClaim(CONTENT).asString()));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<Object> refresh(ServerHttpRequest request, ServerHttpResponse response) {
        return verificationAccessToken(request)
                .flatMap(t -> apply(request, response, t));
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> delete(ServerHttpRequest request, ServerHttpResponse response) {
        return verificationAccessToken(request)
                .map(t -> {
                    t.setAttribute("$error", "JWT unable to actively expire.");
                    return t;
                });
    }

    @Override
    public Mono<Object> apply(ServerHttpRequest request, ServerHttpResponse response, AuthForeignMinistryVisaTemplate template) {
        final Date date = new Date(LocalDateTime.now()
                .plusSeconds(EXPIRATION_TIME).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        final String accessToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(template.getId())
                .withClaim(CONTENT, template.serialize())
                // 使用HMAC256算法
                .sign(Algorithm.HMAC256(cipher.getAccessTokenSecret()));
        final String refreshToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(template.getId())
                .withClaim(CONTENT, template.serialize())
                // 使用HMAC256算法
                .sign(Algorithm.HMAC256(cipher.getRefreshTokenSecret()));
        return Mono.just(executeResultHandler(accessToken, refreshToken));
    }

}
