package club.p6e.coat.auth.client.cache.redis;

import club.p6e.coat.auth.client.cache.AuthStateCacheReactive;
import club.p6e.coat.auth.client.cache.redis.support.RedisCacheReactive;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnBean(ReactiveStringRedisTemplate.class)
@ConditionalOnClass(name = "org.springframework.web.reactive.package-info")
@ConditionalOnProperty(name = "club.p6e.coat.auth.client.cache.type", havingValue = "REDIS")
public class AuthStateRedisCacheReactive extends RedisCacheReactive implements AuthStateCacheReactive {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public AuthStateRedisCacheReactive(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return template.delete(CACHE_PREFIX + key);
    }

    @Override
    public Mono<String> get(String key) {
        return template.opsForValue().get(CACHE_PREFIX + key);
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return template.opsForValue().set(CACHE_PREFIX + key, value, Duration.ofSeconds(EXPIRATION_TIME));
    }

    @Override
    public Mono<String> getAndDel(String key) {
        return get(key).flatMap(v -> del(key).map(l -> v));
    }

}
