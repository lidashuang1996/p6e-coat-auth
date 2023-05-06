package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.AuthException;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 外交部
 * 通过 HTTP -> HEADER 的实现
 * 本类采用的是 REDIS 缓存的方式进行实现
 * 本类采用的前端缓存方式为 HEADER/LOCALSTORAGE 进行缓存数据
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AuthForeignMinistry.class,
        ignored = ReactiveHttpHeaderRedisCacheAuthForeignMinistryImpl.class
)
public class ReactiveHttpHeaderRedisCacheAuthForeignMinistryImpl
        extends ReactiveHttpHeaderAuthForeignMinistry implements AuthForeignMinistry {

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
    public ReactiveHttpHeaderRedisCacheAuthForeignMinistryImpl(
            AuthCache cache,
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public AuthForeignMinistryVisaTemplate verificationAccessToken(ServerHttpRequest request) {
        final String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(accessToken.trim());
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
    public AuthForeignMinistryVisaTemplate verificationRefreshToken(ServerHttpRequest request) {
        final String refreshToken = getRefreshToken(request);
        if (refreshToken != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getRefreshToken(refreshToken.trim());
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
    public Object refresh(ServerHttpRequest request, ServerHttpResponse response) {
        final AuthForeignMinistryVisaTemplate template = delete(request, response);
        return apply(request, response, template);
    }

    @Override
    public AuthForeignMinistryVisaTemplate delete(ServerHttpRequest request, ServerHttpResponse response) {
        final String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                final Optional<AuthCache.Token> tokenOptional = cache.getAccessToken(accessToken.trim());
                if (tokenOptional.isPresent()) {
                    final AuthCache.Token token = tokenOptional.get();
                    try {
                        final Optional<String> userOptional = cache.get(token.getUid());
                        if (userOptional.isPresent()) {
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
    public Mono<Object> apply(ServerHttpRequest request, ServerHttpResponse response, AuthForeignMinistryVisaTemplate template) {
        final String uid = template.getId();
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        final AuthCache.Token token = cache.set(uid, template.serialize(), accessToken, refreshToken);
        return Mono.just(executeResultHandler(token.getAccessToken(), token.getRefreshToken()));
    }

}
