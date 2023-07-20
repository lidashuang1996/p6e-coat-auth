package club.p6e.auth.cache.redis;

import club.p6e.auth.cache.redis.support.RedisCache;
import club.p6e.auth.cache.VoucherCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 凭证缓存的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VoucherRedisCache extends RedisCache implements VoucherCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public VoucherRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return template.delete(CACHE_PREFIX + key);
    }

    @Override
    public Mono<Map<String, String>> get(String key) {
        return template
                .opsForHash()
                .entries(CACHE_PREFIX + key)
                .collectList()
                .map(list -> {
                    final Map<String, String> map = new HashMap<>(list.size());
                    list.forEach(item -> map.put((String) item.getKey(), (String) item.getValue()));
                    return map;
                });
    }

    @Override
    public Mono<Boolean> set(String key, Map<String, String> data) {
        return template
                .opsForHash()
                .putAll(CACHE_PREFIX + key, data)
                .flatMap(b -> {
                    if (b) {
                        return template.expire(CACHE_PREFIX + key,
                                Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
                    } else {
                        return Mono.just(false);
                    }
                });
    }

}
