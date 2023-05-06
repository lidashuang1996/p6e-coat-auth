package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.support.ICache;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RedisCache implements ICache {
    @Override
    public String toType() {
        return "REDIS_TYPE";
    }
}
