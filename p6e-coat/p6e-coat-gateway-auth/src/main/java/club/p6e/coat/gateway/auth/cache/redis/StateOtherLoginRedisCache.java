package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.StateOtherLoginCache;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class StateOtherLoginRedisCache implements StateOtherLoginCache {

    @Override
    public Mono<Long> del(String type, String key) {
        return null;
    }

    @Override
    public Mono<String> get(String type, String key) {
        return null;
    }

    @Override
    public Mono<Boolean> set(String type, String key, String value) {
        return null;
    }

}
