package club.p6e.coat.gateway.auth.cache;

import reactor.core.publisher.Mono;

/**
 * 二维码登录的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QrCodeLoginCache {

    /**
     * 过期的时间
     */
    long EXPIRATION_TIME = 320;

    /**
     * 空的内容标识
     */
    String EMPTY_CONTENT = "__null__";

    /**
     * 二维码登录的缓存前缀
     */
    String CACHE_PREFIX = "LOGIN:QR_CODE:";

    /**
     * 条件注册的条件表达式
     */
    String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 是否为空判断
     *
     * @param content 待判断内容
     * @return 是否为空判断结果
     */
    static boolean isEmpty(String content) {
        return EMPTY_CONTENT.equalsIgnoreCase(content);
    }

    /**
     * 是否不为空判断
     *
     * @param content 待判断内容
     * @return 是否不为空判断结果
     */
    static boolean isNotEmpty(String content) {
        return !isEmpty(content);
    }

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
