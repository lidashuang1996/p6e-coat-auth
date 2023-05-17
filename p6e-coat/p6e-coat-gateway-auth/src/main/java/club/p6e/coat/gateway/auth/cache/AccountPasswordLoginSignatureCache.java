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
    public static final long EXPIRATION_TIME = 180;

    /**
     * 用户缓存前缀
     */
    public static final String CACHE_PREFIX = "LOGIN:ACCOUNT_PASSWORD_SIGNATURE_RSA:";

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{"
            + "${p6e.auth.login.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable-transmission-encryption:false}"
            + "}";

    /**
     * 删除数据
     *
     * @param key 键
     */
    public Mono<Long> del(String key);

    /**
     * 读取数据
     *
     * @param key 键
     * @return 读取的数据
     */
    public Mono<String> get(String key);

    /**
     * 写入数据
     *
     * @param key   键
     * @param value 值
     */
    public Mono<Boolean> set(String key, String value);

}
