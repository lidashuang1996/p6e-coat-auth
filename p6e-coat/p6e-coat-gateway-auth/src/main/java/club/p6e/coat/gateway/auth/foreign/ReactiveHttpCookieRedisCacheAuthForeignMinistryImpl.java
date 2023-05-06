package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

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
    public AuthForeignMinistryVisaTemplate verificationAccessToken(HttpServletRequest request) {
        final Cookie cookie = getAccessTokenCookie(request);
        if (cookie != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(cookie.getValue().trim());
                if (tokenOptional.isPresent()) {
                    final Optional<String> userOptional = cache.get(tokenOptional.get().getUid());
                    if (userOptional.isPresent()) {
                        return ForeignMinistryVisaTemplate.deserialization(userOptional.get());
                    }
                }
            } catch (Exception e) {
                throw GlobalExceptionContext.executeAuthException(
                        this.getClass(), "fun verificationAccessToken(HttpServletRequest request).");
            }
        }
        throw GlobalExceptionContext.executeAuthException(
                this.getClass(), "fun verificationAccessToken(HttpServletRequest request).");
    }

    @Override
    public AuthForeignMinistryVisaTemplate verificationRefreshToken(HttpServletRequest request) {
        final Cookie cookie = getRefreshTokenCookie(request);
        if (cookie != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getRefreshToken(cookie.getValue().trim());
                if (tokenOptional.isPresent()) {
                    final Optional<String> userOptional = cache.get(tokenOptional.get().getUid());
                    if (userOptional.isPresent()) {
                        return ForeignMinistryVisaTemplate.deserialization(userOptional.get());
                    }
                }
            } catch (Exception e) {
                throw GlobalExceptionContext.executeAuthException(
                        this.getClass(), "fun verificationRefreshToken(HttpServletRequest request).");
            }
        }
        throw GlobalExceptionContext.executeAuthException(
                this.getClass(), "fun verificationRefreshToken(HttpServletRequest request).");
    }


    @Override
    public Object refresh(HttpServletRequest request, HttpServletResponse response) {
        final ForeignMinistryVisaTemplate foreignMinistryVisaTemplate = delete(request, response);
        return apply(request, response, foreignMinistryVisaTemplate);
    }

    @Override
    public AuthForeignMinistryVisaTemplate delete(HttpServletRequest request, HttpServletResponse response) {
        final Cookie cookie = getAccessTokenCookie(request);
        if (cookie != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(cookie.getValue().trim());
                if (tokenOptional.isPresent()) {
                    final AuthCache.Token token = tokenOptional.get();
                    try {
                        final Optional<String> userOptional = cache.get(token.getUid());
                        if (userOptional.isPresent()) {
                            final Cookie accessCookie = new Cookie(AUTH_COOKIE_ACCESS_TOKEN_NAME, COOKIE_EMPTY);
                            accessCookie.setMaxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            accessCookie.setHttpOnly(true);
                            final Cookie refreshCookie = new Cookie(AUTH_COOKIE_REFRESH_TOKEN_NAME, COOKIE_EMPTY);
                            refreshCookie.setMaxAge(COOKIE_EMPTY_EXPIRATION_TIME);
                            refreshCookie.setHttpOnly(true);
                            response.addCookie(accessCookie);
                            response.addCookie(refreshCookie);
                            return ForeignMinistryVisaTemplate.deserialization(userOptional.get());
                        }
                    } finally {
                        cache.cleanToken(token.getAccessToken());
                    }
                }
            } catch (Exception e) {
                throw GlobalExceptionContext.executeAuthException(
                        this.getClass(), "fun delete(HttpServletRequest request, HttpServletResponse response).");
            }
        }
        throw GlobalExceptionContext.executeAuthException(
                this.getClass(), "fun delete(HttpServletRequest request, HttpServletResponse response).");
    }

    @Override
    public Mono<Object> apply(HttpServletRequest request, HttpServletResponse response, ForeignMinistryVisaTemplate template) {
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        final AuthCache.Token token = cache.set(template.getId(), template.serialize(), accessToken, refreshToken);
        final Cookie accessCookie = new Cookie(AUTH_COOKIE_ACCESS_TOKEN_NAME, token.getAccessToken());
        accessCookie.setHttpOnly(true);
        accessCookie.setMaxAge(COOKIE_EXPIRATION_TIME);
        final Cookie refreshCookie = new Cookie(AUTH_COOKIE_REFRESH_TOKEN_NAME, token.getAccessToken());
        refreshCookie.setHttpOnly(true);
        refreshCookie.setMaxAge(COOKIE_EXPIRATION_TIME);
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        return SUCCESS_RESULT;
    }

}
