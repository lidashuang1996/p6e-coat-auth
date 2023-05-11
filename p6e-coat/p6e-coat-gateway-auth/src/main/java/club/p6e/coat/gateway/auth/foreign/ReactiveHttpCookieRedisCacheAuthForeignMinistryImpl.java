package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.AuthException;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.Optional;

/**
 * 外交部
 * 通过 HTTP -> COOKIE 的实现
 * 本类采用的是 REDIS 缓存的方式进行实现
 * 本类采用的前端缓存方式为 COOKIE 进行缓存数据
 *
 * @author lidashuang
 * @version 1.0
 */
public class ReactiveHttpCookieRedisCacheAuthForeignMinistryImpl
        extends ReactiveHttpCookieAuthForeignMinistry implements AuthForeignMinistry {

    /**
     * 认证缓存对象
     */
    private final AuthCache cache;

    /**
     * 令牌生成器
     */
    private final AuthAccessTokenGenerator accessTokenGenerator;

    /**
     * 刷新令牌生成器
     */
    private final AuthRefreshTokenGenerator refreshTokenGenerator;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存对象
     */
    public ReactiveHttpCookieRedisCacheAuthForeignMinistryImpl(
            AuthCache cache,
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }


    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationAccessToken(ServerHttpRequest request) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                return cache
                        .getAccessToken(value)
                        .flatMap(t -> cache.getUser(t.getUid()))
                        .map(AuthForeignMinistryVisaTemplate::deserialization);
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> verificationRefreshToken(ServerHttpRequest request) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                return cache
                        .getRefreshToken(value)
                        .flatMap(t -> cache.getUser(t.getUid()))
                        .map(AuthForeignMinistryVisaTemplate::deserialization);
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<Object> refresh(ServerHttpRequest request, ServerHttpResponse response) {
        return delete(request, response)
                .flatMap(t -> apply(request, response, t));
    }

    @Override
    public Mono<AuthForeignMinistryVisaTemplate> delete(ServerHttpRequest request, ServerHttpResponse response) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                return cache
                        .getAccessToken(value)
                        .flatMap(t -> {
                            final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                                    ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                            accessCookieBuilder.httpOnly(true);
                            accessCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                                    ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                            refreshCookieBuilder.httpOnly(true);
                            refreshCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            response.getCookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, refreshCookieBuilder.build());
                            response.getCookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
                            return cache
                                    .getUser(t.getUid())
                                    .flatMap(s -> cache
                                            .cleanToken(t.getAccessToken())
                                            .map(l -> AuthForeignMinistryVisaTemplate.deserialization(s))
                                    );
                        });
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<Object> apply(ServerHttpRequest request, ServerHttpResponse response, AuthForeignMinistryVisaTemplate template) {
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        return cache
                .set(template.getId(), "", template.serialize(), accessToken, refreshToken)
                .map(t -> {
                    final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                            ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, t.getAccessToken());
                    accessCookieBuilder.httpOnly(true);
                    accessCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
                    final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                            ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, t.getRefreshToken());
                    refreshCookieBuilder.httpOnly(true);
                    refreshCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
                    response.getCookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, refreshCookieBuilder.build());
                    response.getCookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
                    return SUCCESS_RESULT;
                });
    }

}
