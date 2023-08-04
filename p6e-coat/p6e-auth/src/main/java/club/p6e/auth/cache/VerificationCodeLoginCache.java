package club.p6e.auth.cache;

import club.p6e.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface VerificationCodeLoginCache extends ICache {

    /**
     * 过期的时间
     */
    long EXPIRATION_TIME = 200L;

    /**
     * 缓存前缀
     */
    String CACHE_PREFIX = "LOGIN:VERIFICATION_CODE:";

    /**
     * 删除数据
     *
     * @param key 键
     * @return 删除数据的条数
     */
    public Mono<Long> del(String key);

    /**
     * 读取数据
     *
     * @param key 键
     * @return 读取的列表数据
     */
    public Mono<List<String>> get(String key);

    /**
     * 写入数据
     *
     * @param key   键
     * @param value 值
     * @return 是否写入数据成功
     */
    public Mono<Boolean> set(String key, String value);

}
