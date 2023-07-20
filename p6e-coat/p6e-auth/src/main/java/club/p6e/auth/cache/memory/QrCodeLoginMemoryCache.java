package club.p6e.auth.cache.memory;

import club.p6e.auth.cache.QrCodeLoginCache;
import club.p6e.auth.cache.memory.support.MemoryCache;
import club.p6e.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

/**
 * 二维码登录的缓存
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginMemoryCache extends MemoryCache implements QrCodeLoginCache {

    private final ReactiveMemoryTemplate template;

    public QrCodeLoginMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @Override
    public Mono<String> get(String key) {
        final String r = template.get(CACHE_PREFIX + key, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return Mono.just(template.set(CACHE_PREFIX + key, value, EXPIRATION_TIME));
    }

}
