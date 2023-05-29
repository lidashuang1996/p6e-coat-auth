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
    public static final String CACHE_PREFIX = "VOUCHER:";

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 60 * 30;

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} || ${p6e.auth.oauth2.enable:false} || ${p6e.auth.register.enable:false}}";

    /**
     * 删除数据
     *
     * @param content 会话编号
     */
    public Mono<Long> del(String content);

    /**
     * 读取全部数据
     *
     * @param content 会话编号
     * @return 会话全部数据
     */
    public Mono<Map<String, String>> get(String content);

    /**
     * 绑定数据
     *
     * @param content 会话编号
     */
    public Mono<Boolean> bind(String content, Map<String, String> data);

}