package club.p6e.coat.auth.cache.redis;

import club.p6e.coat.auth.cache.redis.support.RedisCache;
import club.p6e.coat.auth.cache.PasswordSignatureCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PasswordSignatureRedisCache
        extends RedisCache implements PasswordSignatureCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public PasswordSignatureRedisCache(ReactiveStringRedisTemplate template) {
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
        return template
                .opsForValue()
                .set(CACHE_PREFIX + key, value, Duration.ofSeconds(EXPIRATION_TIME));
    }

}
