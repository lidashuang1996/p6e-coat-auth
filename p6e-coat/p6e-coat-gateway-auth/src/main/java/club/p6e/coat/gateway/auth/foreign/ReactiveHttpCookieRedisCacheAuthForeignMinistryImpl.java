package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.AuthException;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
    public AuthForeignMinistryVisaTemplate verificationAccessToken(ServerRequest request) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(value);
                if (tokenOptional.isPresent()) {
                    final Optional<String> userOptional = cache.get(tokenOptional.get().getUid());
                    if (userOptional.isPresent()) {
                        return AuthForeignMinistryVisaTemplate.deserialization(userOptional.get());
                    }
                }
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public AuthForeignMinistryVisaTemplate verificationRefreshToken(ServerRequest request) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                final Optional<AuthCache.Token> tokenOptional = cache.getRefreshToken(value);
                if (tokenOptional.isPresent()) {
                    final Optional<String> userOptional = cache.get(tokenOptional.get().getUid());
                    if (userOptional.isPresent()) {
                        return AuthForeignMinistryVisaTemplate.deserialization(userOptional.get());
                    }
                }
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }


    @Override
    public Object refresh(ServerRequest request, ServerResponse response) {
        final AuthForeignMinistryVisaTemplate foreignMinistryVisaTemplate = delete(request, response);
        return apply(request, response, foreignMinistryVisaTemplate);
    }

    @Override
    public AuthForeignMinistryVisaTemplate delete(ServerRequest request, ServerResponse response) {
        final List<HttpCookie> cookies = getAccessTokenCookie(request);
        if (cookies != null && cookies.size() > 0) {
            try {
                final String value = cookies.get(0).getValue().trim();
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(value);
                if (tokenOptional.isPresent()) {
                    final AuthCache.Token token = tokenOptional.get();
                    try {
                        final Optional<String> userOptional = cache.get(token.getUid());
                        if (userOptional.isPresent()) {
                            final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                                    ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                            accessCookieBuilder.httpOnly(true);
                            accessCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                                    ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, COOKIE_EMPTY_CONTENT);
                            refreshCookieBuilder.httpOnly(true);
                            refreshCookieBuilder.maxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            response.cookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, refreshCookieBuilder.build());
                            response.cookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
                            return AuthForeignMinistryVisaTemplate.deserialization(userOptional.get());
                        }
                    } finally {
                        cache.cleanToken(token.getAccessToken());
                    }
                }
            } catch (Exception e) {
                // 忽略异常
            }
        }
        throw new AuthException(this.getClass(), "fun verificationAccessToken(HttpServletRequest request).", "");
    }

    @Override
    public Mono<Object> apply(ServerRequest request, ServerResponse response, AuthForeignMinistryVisaTemplate template) {
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        final AuthCache.Token token = cache.set(template.getId(), template.serialize(), accessToken, refreshToken);
        final ResponseCookie.ResponseCookieBuilder accessCookieBuilder =
                ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, token.getAccessToken());
        accessCookieBuilder.httpOnly(true);
        accessCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
        final ResponseCookie.ResponseCookieBuilder refreshCookieBuilder =
                ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, token.getAccessToken());
        refreshCookieBuilder.httpOnly(true);
        refreshCookieBuilder.maxAge(COOKIE_EXPIRATION_TIME);
        response.cookies().set(AUTH_COOKIE_ACCESS_TOKEN_NAME, refreshCookieBuilder.build());
        response.cookies().set(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshCookieBuilder.build());
        return Mono.just(SUCCESS_RESULT);
    }

}
