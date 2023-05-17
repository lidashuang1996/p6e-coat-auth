package club.p6e.coat.gateway.auth.cache;

import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 二维码登录的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QuickResponseCodeLoginCache {

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 300;

    /**
     * 空的内容标识
     */
    public static final String EMPTY_CONTENT = "__NULL__";

    /**
     * 二维码登录的缓存前缀
     */
    public static final String CACHE_PREFIX = "LOGIN:QR_CODE:";

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 是否为空判断
     *
     * @param content 待判断内容
     * @return 是否为空判断结果
     */
    public static boolean isEmpty(String content) {
        return EMPTY_CONTENT.equals(content);
    }
    public static boolean isNotEmpty(String content) {
        return !isEmpty(content);
    }


    /**
     * 删除
     *
     * @param key 键
     */
    public Mono<Long> del(String key);

    /**
     * 写入
     *
     * @param key   键
     * @param value 值
     */
    public Mono<Boolean> set(String key, String value);

    /**
     * 读取
     *
     * @param key 键
     * @return 读取的内容
     */
    public Mono<String> get(String key);

}
