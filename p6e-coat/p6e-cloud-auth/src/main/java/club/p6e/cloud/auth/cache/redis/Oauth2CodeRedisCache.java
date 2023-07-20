package club.p6e.cloud.auth.cache.redis;

import club.p6e.cloud.auth.cache.redis.support.RedisCache;
import club.p6e.cloud.auth.cache.Oauth2CodeCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * OAUTH2 CODE 授权模式缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2CodeRedisCache extends RedisCache implements Oauth2CodeCache {
    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;


    public Oauth2CodeRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return template.delete(CACHE_PREFIX + key);
    }

    @Override
    public Mono<Boolean> set(String key, Map<String, String> map) {
        return template
                .opsForHash()
                .putAll(CACHE_PREFIX + key, map)
                .flatMap(b -> template.expire(CACHE_PREFIX + key, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                .map(b -> true);
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


}
