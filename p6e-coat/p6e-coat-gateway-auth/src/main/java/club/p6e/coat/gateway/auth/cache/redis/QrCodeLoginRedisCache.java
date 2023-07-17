package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.cache.redis.support.RedisCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 二维码登录的缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginRedisCache extends RedisCache implements QrCodeLoginCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public QrCodeLoginRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return template.delete(CACHE_PREFIX + key);
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return template.opsForValue().set(CACHE_PREFIX + key,
                value, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

    @Override
    public Mono<String> get(String key) {
        return template.opsForValue().get(CACHE_PREFIX + key);
    }

}
