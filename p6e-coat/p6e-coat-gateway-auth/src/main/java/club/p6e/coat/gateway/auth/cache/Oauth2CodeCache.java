package club.p6e.coat.gateway.auth.cache;

import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * OAUTH2 CODE 授权模式缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2CodeCache {

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 60 * 5;

    /**
     * OAUTH2 CODE 模式的缓存前缀
     */
    public static final String CACHE_PREFIX = "OAUTH2:CODE:";

    /**
     * OAUTH2 作用域
     */
    public static final String OAUTH2_SCOPE = "OAUTH2_SCOPE";

    /**
     * OAUTH2 客户端编号
     */
    public static final String OAUTH2_CLIENT_ID = "OAUTH2_CLIENT_ID";

    /**
     * OAUTH2 重定向 URI
     */
    public static final String OAUTH2_REDIRECT_URI = "OAUTH2_REDIRECT_URI";

    /**
     * OAUTH2 用户编号
     */
    public static final String OAUTH2_USER_ID = "OAUTH2_USER_ID";

    /**
     * OAUTH2 用户信息
     */
    public static final String OAUTH2_USER_INFO = "OAUTH2_USER_INFO";

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    /**
     * 删除
     *
     * @param key 键
     */
    public Mono<Long> del(String key);

    /**
     * 创建
     *
     * @param key 键
     * @param map key/value
     */
    public Mono<Boolean> set(String key, Map<String, String> map);

    /**
     * 读取
     *
     * @param key 键
     * @param kv  HASH 键
     * @return 读取的内容
     */
    public Mono<Map<String, String>> get(String key);

}
