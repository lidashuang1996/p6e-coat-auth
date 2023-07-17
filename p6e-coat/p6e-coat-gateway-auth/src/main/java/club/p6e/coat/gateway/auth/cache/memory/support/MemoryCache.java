package club.p6e.coat.gateway.auth.cache.memory.support;

import club.p6e.coat.gateway.auth.cache.support.ICache;

/**
 * @author lidashuang
 * @version 1.0
 */
public class MemoryCache implements ICache {

    @Override
    public String toType() {
        return "MEMORY_TYPE";
    }

}
