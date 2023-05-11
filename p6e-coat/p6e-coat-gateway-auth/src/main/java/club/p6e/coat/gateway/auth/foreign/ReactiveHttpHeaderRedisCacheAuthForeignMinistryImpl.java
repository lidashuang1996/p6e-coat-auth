package club.p6e.coat.gateway.auth.foreign;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.AuthException;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.ResponseCookie;
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
    public Mono<AuthForeignMinistryVisaTemplate> verificationAccessToken(ServerHttpRequest request) {
        final String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                return cache
                        .getAccessToken(accessToken)
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
        final String refreshToken = getRefreshToken(request);
        if (refreshToken != null) {
            try {
                return cache
                        .getRefreshToken(refreshToken)
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
        final String accessToken = getAccessToken(request);
        if (accessToken != null) {
            try {
                return cache
                        .getAccessToken(accessToken)
                        .flatMap(t -> cache
                                .getUser(t.getUid())
                                .flatMap(s -> cache
                                        .cleanToken(t.getAccessToken())
                                        .map(l -> AuthForeignMinistryVisaTemplate.deserialization(s))
                                ));
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
                .set(template.getId(), "", accessToken, refreshToken, template.serialize())
                .map(t -> executeResultHandler(t.getAccessToken(), t.getRefreshToken()));
    }

}
