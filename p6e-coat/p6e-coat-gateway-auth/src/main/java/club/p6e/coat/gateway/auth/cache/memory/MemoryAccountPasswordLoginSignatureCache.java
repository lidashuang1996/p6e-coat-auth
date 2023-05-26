package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class MemoryAccountPasswordLoginSignatureCache
        extends MemoryCache implements AccountPasswordLoginSignatureCache {

    @Override
    public Mono<Long> del(String key) {
        return null;
    }

    @Override
    public Mono<String> get(String key) {
        return null;
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return null;
    }

}
