package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.VoucherCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * 凭证会话默认的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
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
    public Mono<Long> del(String content) {
        return template.delete(CACHE_PREFIX + content);
    }

    @Override
    public Mono<Map<String, String>> get(String content) {
        return template
                .opsForHash()
                .entries(CACHE_PREFIX + content)
                .collectList()
                .map(list -> {
                    final Map<String, String> map = new HashMap<>(list.size());
                    list.forEach(item -> map.put((String) item.getKey(), (String) item.getValue()));
                    return map;
                });
    }

    @Override
    public Mono<Boolean> bind(String content, Map<String, String> data) {
        return template
                .opsForHash()
                .putAll(CACHE_PREFIX + content, data)
                .flatMap(b -> {
                    if (b) {
                        return template.expire(CACHE_PREFIX + content,
                                Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
                    } else {
                        return Mono.just(false);
                    }
                });
    }

}
