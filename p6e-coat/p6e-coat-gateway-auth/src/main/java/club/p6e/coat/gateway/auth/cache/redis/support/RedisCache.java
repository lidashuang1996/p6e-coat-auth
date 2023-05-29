package club.p6e.coat.gateway.auth.cache.redis.support;

import club.p6e.coat.gateway.auth.cache.support.ICache;

/**
 * Redis 抽象缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class RedisCache implements ICache {

    @Override
    public String toType() {
        return "REDIS_TYPE";
    }

}
