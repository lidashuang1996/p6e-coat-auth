package club.p6e.auth.cache;

import club.p6e.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 验证码登录的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VerificationCodeLoginCache extends ICache {

    /**
     * 分割符号
     */
    public static final String DELIMITER = ":";

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 200L;

    /**
     * 验证码登录的缓存前缀
     */
    public static final String CACHE_PREFIX = "LOGIN:VERIFICATION_CODE:";

    /**
     * 删除数据
     *
     * @param key  键
     * @param type 类型
     * @return 删除数据的条数
     */
    public Mono<Long> del(String type, String key);

    /**
     * 删除数据
     *
     * @param key   键
     * @param type  类型
     * @param value 值
     * @return 删除数据的条数
     */
    public Mono<Long> del(String type, String key, String value);

    /**
     * 读取数据
     *
     * @param key  键
     * @param type 类型
     * @return 读取的列表数据
     */
    public Mono<List<String>> get(String type, String key);

    /**
     * 写入数据
     *
     * @param type  类型
     * @param key   键
     * @param value 值
     * @return 是否写入数据成功
     */
    public Mono<Boolean> set(String type, String key, String value);

}
