package club.p6e.auth.cache.memory;

import club.p6e.auth.cache.RegisterCodeCache;
import club.p6e.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterCodeMemoryCache implements RegisterCodeCache {

    private final ReactiveMemoryTemplate template;

    public RegisterCodeMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @Override
    public Mono<List<String>> get(String key) {
        final Map<String, String> data = get0(key);
        template.set(CACHE_PREFIX + key, data, EXPIRATION_TIME);
        return data.size() == 0 ? Mono.empty() : Mono.just(new ArrayList<>(data.keySet()));
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        final Map<String, String> data = get0(key);
        data.put(value, String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        return Mono.just(template.set(CACHE_PREFIX + key, data, EXPIRATION_TIME));
    }

    @SuppressWarnings("ALL")
    public Map<String, String> get0(String key) {
        final Map map = template.get(CACHE_PREFIX + key, Map.class);
        if (map == null) {
            return new HashMap<>();
        } else {
            final long now = System.currentTimeMillis();
            final Map<String, String> result = new HashMap<>();
            final Map<String, String> data = (Map<String, String>) map;
            for (final String k : data.keySet()) {
                final String v = data.get(k);
                final long lv = Long.valueOf(v);
                if (now <= lv) {
                    result.put(k, v);
                }
            }
            return result;
        }
    }
}
