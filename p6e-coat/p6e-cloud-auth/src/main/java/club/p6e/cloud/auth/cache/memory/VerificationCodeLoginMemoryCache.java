package club.p6e.cloud.auth.cache.memory;

import club.p6e.cloud.auth.cache.VerificationCodeLoginCache;
import club.p6e.cloud.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.cloud.auth.cache.memory.support.MemoryCache;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeLoginMemoryCache
        extends MemoryCache implements VerificationCodeLoginCache {

    private final ReactiveMemoryTemplate template;

    public VerificationCodeLoginMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String type, String key) {
        return Mono.just(template.del(CACHE_PREFIX + type + DELIMITER + key));
    }

    @Override
    public Mono<Long> del(String type, String key, String value) {
        final Map<String, String> map = get0(type, key);
        if (map == null || map.size() == 0) {
            return Mono.just(0L);
        } else {
            if (map.get(value) == null) {
                return Mono.just(0L);
            } else {
                map.remove(value);
                if (map.size() == 0) {
                    return del(key, type);
                } else {
                    return Mono.just(template.set(CACHE_PREFIX + type + DELIMITER + key, map, EXPIRATION_TIME) ? 1L : 0L);
                }
            }
        }
    }

    @Override
    public Mono<List<String>> get(String type, String key) {
        final Map<String, String> map = get0(type, key);
        if (map == null || map.size() == 0) {
            return Mono.empty();
        } else {
            return Mono.just(new ArrayList<>(map.keySet()));
        }
    }

    @Override
    public Mono<Boolean> set(String type, String key, String value) {
        final Map<String, String> map = get0(key, type);
        map.put(value, String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        return Mono.just(template.set(CACHE_PREFIX + type + DELIMITER + key, map, EXPIRATION_TIME));
    }

    @SuppressWarnings("ALL")
    private Map<String, String> get0(String type, String key) {
        Map map = template.get(CACHE_PREFIX + type + DELIMITER + key, Map.class);
        if (map == null) {
            return new HashMap<>();
        } else {
            final Map<String, String> result = (Map<String, String>) map;
            final long now = System.currentTimeMillis();
            for (final String k : result.keySet()) {
                if (map.get(k) != null
                        && now < Long.parseLong(result.get(k))) {
                    map.remove(k);
                }
            }
            return result;
        }
    }

}
