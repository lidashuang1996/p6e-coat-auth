package club.p6e.cloud.auth.cache.memory.support;

import club.p6e.cloud.auth.cache.support.ICache;

/**
 * 内存缓存的类型声明类
 *
 * @author lidashuang
 * @version 1.0
 */
public class MemoryCache implements ICache {

    @Override
    public String toType() {
        return "MEMORY_TYPE";
    }

}
