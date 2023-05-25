package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.cache.support.ICache;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class MemoryCache implements ICache {

    protected final Map<String, String> sMap = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public String toType() {
        return "MemoryCache";
    }

}
