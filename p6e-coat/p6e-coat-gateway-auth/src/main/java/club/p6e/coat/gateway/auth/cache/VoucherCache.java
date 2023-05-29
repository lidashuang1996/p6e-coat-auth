package club.p6e.coat.gateway.auth.cache;

import club.p6e.coat.gateway.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 凭证缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VoucherCache extends ICache {

    /**
     * 缓存前缀
     */
    String CACHE_PREFIX = "VOUCHER:";

    /**
     * 过期的时间
     */
    long EXPIRATION_TIME = 900L;

    /**
     * 条件注册的条件表达式
     */
    String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} || ${p6e.auth.oauth2.enable:false} || ${p6e.auth.register.enable:false}}";

    /**
     * 删除数据
     *
     * @param key 键
     * @return 删除数据的条数
     */
    Mono<Long> del(String key);

    /**
     * 读取数据
     *
     * @param key 键
     * @return 值
     */
    Mono<Map<String, String>> get(String key);

    /**
     * 写入数据
     *
     * @param key  键
     * @param data 值
     * @return 是否写入数据成功
     */
    Mono<Boolean> set(String key, Map<String, String> data);

}