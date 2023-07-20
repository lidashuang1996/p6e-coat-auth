package club.p6e.cloud.auth.cache.redis.support;

import club.p6e.cloud.auth.cache.support.ICache;

/**
 * redis 缓存的类型声明类
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
