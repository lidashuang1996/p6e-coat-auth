package club.p6e.coat.auth.cache.memory;

import club.p6e.coat.auth.cache.QrCodeWebSocketAuthCache;
import club.p6e.coat.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeWebSocketAuthMemoryCache implements QrCodeWebSocketAuthCache {
    /**
     * 内存缓存模板对象
     */
    private final ReactiveMemoryTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 内存缓存模板对象
     */
    public QrCodeWebSocketAuthMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @Override
    public Mono<String> get(String key) {
        final String value = template.get(CACHE_PREFIX + key, String.class);
        return value == null ? Mono.empty() : Mono.just(value);
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return Mono.just(template.set(CACHE_PREFIX + key, value, EXPIRATION_TIME));
    }

}
