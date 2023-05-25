package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class RedisCache implements ICache {
    @Override
    public String toType() {
        return "REDIS_TYPE";
    }

}
