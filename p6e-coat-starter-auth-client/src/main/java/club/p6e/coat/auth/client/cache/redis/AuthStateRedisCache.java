package club.p6e.coat.auth.client.cache.redis;

import club.p6e.coat.auth.client.cache.AuthStateCache;
import club.p6e.coat.auth.client.cache.redis.support.RedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnClass(name = "org.springframework.web.servlet.package-info")
@ConditionalOnProperty(name = "club.p6e.coat.auth.client.cache.type", havingValue = "REDIS")
public class AuthStateRedisCache extends RedisCache implements AuthStateCache {

    /**
     * 缓存对象
     */
    private final StringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public AuthStateRedisCache(StringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Boolean del(String key) {
        return template.delete(CACHE_PREFIX + key);
    }

    @Override
    public String get(String key) {
        return template.opsForValue().get(CACHE_PREFIX + key);
    }

    @Override
    public Boolean set(String key, String value) {
        template.opsForValue().set(CACHE_PREFIX + key, value, Duration.ofSeconds(EXPIRATION_TIME));
        return true;
    }

    @Override
    public String getAndDel(String key) {
        final String value = get(key);
        if (value != null) {
            del(key);
        }
        return value;

    }

}
