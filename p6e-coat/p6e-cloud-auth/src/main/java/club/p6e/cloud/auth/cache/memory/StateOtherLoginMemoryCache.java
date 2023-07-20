package club.p6e.cloud.auth.cache.memory;

import club.p6e.cloud.auth.cache.StateOtherLoginCache;
import club.p6e.cloud.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class StateOtherLoginMemoryCache implements StateOtherLoginCache {

    private final ReactiveMemoryTemplate template;

    public StateOtherLoginMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String type, String key) {
        return Mono.just(template.del(CACHE_PREFIX + type + DELIMITER + key));
    }

    @Override
    public Mono<String> get(String type, String key) {
        final String r = template.get(CACHE_PREFIX + type + DELIMITER + key, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    @Override
    public Mono<Boolean> set(String type, String key, String value) {
        return Mono.just(template.set(CACHE_PREFIX + type + DELIMITER + key, value, EXPIRATION_TIME));
    }

}
