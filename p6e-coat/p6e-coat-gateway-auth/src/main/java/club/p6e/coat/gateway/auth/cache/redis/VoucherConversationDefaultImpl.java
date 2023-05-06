package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.VoucherCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 凭证会话默认的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = VoucherCache.class,
        ignored = VoucherConversationDefaultImpl.class
)
public class VoucherConversationDefaultImpl extends RedisCache implements VoucherCache {

    /**
     * 缓存源的名称
     */
    private static final String CACHE_SOURCE_NAME = "VOUCHER";

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public VoucherConversationDefaultImpl(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public void set(String content, String key, String value) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        stringRedisTemplate.opsForHash().put(CACHE_PREFIX + content, key, value);
//        stringRedisTemplate.expire(CACHE_PREFIX + content, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

    @Override
    public void set(String content, Map<String, String> dataMap) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        stringRedisTemplate.opsForHash().putAll(CACHE_PREFIX + content, dataMap);
//        stringRedisTemplate.expire(CACHE_PREFIX + content, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

    @Override
    public void bind(String content, String key, String value) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        stringRedisTemplate.opsForHash().put(CACHE_PREFIX + content, key, value);
//        stringRedisTemplate.expire(CACHE_PREFIX + content, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

    @Override
    public void bind(String content, Map<String, String> dataMap) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        stringRedisTemplate.opsForHash().putAll(CACHE_PREFIX + content, dataMap);
//        stringRedisTemplate.expire(CACHE_PREFIX + content, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

    @Override
    public void del(String content) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        stringRedisTemplate.delete(CACHE_PREFIX + content);
    }

    @Override
    public Optional<String> get(String content, String key) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        final Object value = stringRedisTemplate.opsForHash().get(CACHE_PREFIX + content, key);
//        return Optional.ofNullable(value == null ? null : String.valueOf(value));
        return null;
    }

    @Override
    public Optional<Map<String, String>> getAll(String content) {
//        final StringRedisTemplate stringRedisTemplate = redisCache.getStringRedisTemplate(CACHE_SOURCE_NAME);
//        return stringRedisTemplate.execute((RedisCallback<Optional<Map<String, String>>>) connection -> {
//            final Map<byte[], byte[]> map = connection.hashCommands()
//                    .hGetAll((CACHE_PREFIX + content).getBytes(StandardCharsets.UTF_8));
//            if (map == null || map.size() == 0) {
//                return Optional.empty();
//            } else {
//                final Map<String, String> result = new HashMap<>(map.size());
//                for (byte[] bytes : map.keySet()) {
//                    result.put(new String(bytes, StandardCharsets.UTF_8),
//                            new String(map.get(bytes), StandardCharsets.UTF_8));
//                }
//                return Optional.of(result);
//            }
//        });
        return null;
    }

}
