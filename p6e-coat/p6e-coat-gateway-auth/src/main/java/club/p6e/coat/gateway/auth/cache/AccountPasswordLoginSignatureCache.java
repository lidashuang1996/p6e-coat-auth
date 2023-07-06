package club.p6e.coat.gateway.auth.cache;

import club.p6e.coat.gateway.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录签名的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AccountPasswordLoginSignatureCache extends ICache {

    /**
     * 过期的时间
     */
    long EXPIRATION_TIME = 180;

    /**
     * 用户缓存前缀
     */
    String CACHE_PREFIX = "LOGIN:ACCOUNT_PASSWORD_SIGNATURE_RSA:";

    /**
     * 条件注册的条件表达式
     */
    String CONDITIONAL_EXPRESSION = "#{"
            + "${p6e.auth.login.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable-transmission-encryption:false} "
            + "}";

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
    Mono<String> get(String key);

    /**
     * 写入数据
     *
     * @param key   键
     * @param value 值
     * @return 是否写入数据成功
     */
    Mono<Boolean> set(String key, String value);

}
