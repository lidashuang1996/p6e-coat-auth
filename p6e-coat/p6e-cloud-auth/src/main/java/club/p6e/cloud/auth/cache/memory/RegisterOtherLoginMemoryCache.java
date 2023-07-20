package club.p6e.cloud.auth.cache.memory;

import club.p6e.cloud.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.cloud.auth.cache.RegisterOtherLoginCache;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterOtherLoginMemoryCache implements RegisterOtherLoginCache {

    private final ReactiveMemoryTemplate template;

    public RegisterOtherLoginMemoryCache(ReactiveMemoryTemplate template) {
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
