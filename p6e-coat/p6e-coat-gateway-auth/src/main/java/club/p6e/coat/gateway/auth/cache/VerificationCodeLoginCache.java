package club.p6e.coat.gateway.auth.cache;

import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * 验证码登录的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VerificationCodeLoginCache {

    /**
     * 分割符号
     */
    public static final String DELIMITER = ":";

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 300;

    /**
     * 验证码登录的缓存前缀
     */
    public static final String CACHE_PREFIX = "LOGIN:VERIFICATION_CODE:";

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.verification-code.enable:false}}";

    /**
     * 删除
     *
     * @param type 类型
     * @param key  键
     */
    public Mono<Long> del(String key, String type);

    /**
     * 删除
     *
     * @param type  类型
     * @param key   键
     * @param value 值
     */
    public Mono<Long> del(String key, String type, String value);

    /**
     * 写入
     *
     * @param type  类型
     * @param key   键
     * @param value 值
     */
    public Mono<Boolean> set(String key, String type, String value);

    /**
     * 读取
     *
     * @param type 类型
     * @param key  键
     * @return 读取的内容
     */
    public Mono<List<String>> get(String key, String type);

}
