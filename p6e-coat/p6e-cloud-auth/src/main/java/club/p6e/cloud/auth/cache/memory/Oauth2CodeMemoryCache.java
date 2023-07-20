package club.p6e.cloud.auth.cache.memory;

import club.p6e.cloud.auth.cache.Oauth2CodeCache;
import club.p6e.cloud.auth.cache.memory.support.MemoryCache;
import club.p6e.cloud.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2CodeMemoryCache extends MemoryCache implements Oauth2CodeCache {

    private final ReactiveMemoryTemplate template;

    public Oauth2CodeMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Map<String, String>> get(String key) {
        final Map r = template.get(CACHE_PREFIX + key, Map.class);
        return r == null ? Mono.empty() : Mono.just((Map<String, String>) r);
    }

    @Override
    public Mono<Boolean> set(String key, Map<String, String> map) {
        return Mono.just(template.set(CACHE_PREFIX + key, map, EXPIRATION_TIME));
    }

}
