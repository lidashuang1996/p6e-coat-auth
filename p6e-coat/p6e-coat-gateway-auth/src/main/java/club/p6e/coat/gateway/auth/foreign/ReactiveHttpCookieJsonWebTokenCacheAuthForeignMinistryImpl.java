package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.AuthForeignMinistry;
import club.p6e.coat.gateway.auth.AuthForeignMinistryVisaTemplate;
import club.p6e.coat.gateway.auth.JsonWebTokenCipher;
import club.p6e.coat.gateway.auth.error.AuthException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;


/**
 * 外交部
 * 通过 HTTP -> COOKIE 的实现
 * 本类采用的是 JWT 的方式进行实现
 * 本类采用的前端缓存方式为 COOKIE 进行缓存数据
 *
 * @author lidashuang
 * @version 1.0
 */
public class ReactiveHttpCookieJsonWebTokenCacheAuthForeignMinistryImpl
        extends ReactiveHttpCookieAuthForeignMinistry implements AuthForeignMinistry {

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
    public ReactiveHttpCookieJsonWebTokenCacheAuthForeignMinistryImpl(JsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationAccessToken(ServerHttpRequest request) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            final String value = cookies.get(0).getValue().trim();
            try {
                final DecodedJWT decoded = JWT.require(
                        Algorithm.HMAC256(cipher.getAccessTokenSecret())).build().verify(value);
                return Mono.just(AuthForeignMinistryVisaTemplate.deserialization(decoded.getClaim(CONTENT).asString()));
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationRefreshToken(ServerHttpRequest request) {
        final List<HttpCookie> cookies = getRefreshTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            final String value = cookies.get(0).getValue().trim();
            try {
                final DecodedJWT decoded = JWT.require(
                        Algorithm.HMAC256(cipher.getRefreshTokenSecret())).build().verify(value);
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
                    // JWT 无法主动过期
                    t.setAttribute("$error", "JWT unable to actively expire.");
                    final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                            ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                    accessCookieBuilder.httpOnly(true);
                    accessCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                    final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                            ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                    refreshCookieBuilder.httpOnly(true);
                    refreshCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                    response.getCookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessCookieBuilder.build());
                    response.getCookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
                    return t;
                });
    }

    @Override
    public Mono<Object> apply(ServerHttpRequest request, ServerHttpResponse response, AuthForeignMinistryVisaTemplate template) {
        final Date date = new Date(LocalDateTime.now()
                .plusSeconds(COOKIE_EXPIRATION_TIME).toInstant(ZoneOffset.of("+8")).toEpochMilli());
        final String accessToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(template.getId())
                .withClaim(CONTENT, template.serialize())
                // 使用HMAC256算法
                .sign(Algorithm.HMAC256(cipher.getAccessTokenSecret()));
        final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessToken);
        accessCookieBuilder.httpOnly(true);
        accessCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
        response.getCookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessCookieBuilder.build());
        final String refreshToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(template.getId())
                .withClaim(CONTENT, template.serialize())
                // 使用HMAC256算法
                .sign(Algorithm.HMAC256(cipher.getRefreshTokenSecret()));
        final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshToken);
        refreshCookieBuilder.httpOnly(true);
        refreshCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
        response.getCookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
        return Mono.just(SUCCESS_RESULT);
    }
}
