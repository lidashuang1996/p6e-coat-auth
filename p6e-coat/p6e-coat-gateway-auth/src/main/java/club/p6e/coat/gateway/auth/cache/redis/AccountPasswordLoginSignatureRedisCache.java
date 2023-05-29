package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.cache.redis.support.RedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 账号密码登录签名的缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AccountPasswordLoginSignatureCache.class,
        ignored = AccountPasswordLoginSignatureRedisCache.class
)
@ConditionalOnExpression(AccountPasswordLoginSignatureCache.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginSignatureRedisCache extends RedisCache implements AccountPasswordLoginSignatureCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public AccountPasswordLoginSignatureRedisCache(ReactiveStringRedisTemplate template) {
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
        return template.opsForValue().set(CACHE_PREFIX + key, value, Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS));
    }

}
