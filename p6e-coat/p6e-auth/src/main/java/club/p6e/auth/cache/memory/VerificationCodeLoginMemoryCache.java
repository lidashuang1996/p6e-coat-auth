package club.p6e.auth.cache.memory;

import club.p6e.auth.cache.VerificationCodeLoginCache;
import club.p6e.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.auth.cache.memory.support.MemoryCache;
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

    /**
     * 内存缓存模板对象
     */
    private final ReactiveMemoryTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 内存缓存模板对象
     */
    public VerificationCodeLoginMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @Override
    public Mono<List<String>> get(String key) {
        final Map<String, String> map = get0(key);
        if (map.isEmpty()) {
            return Mono.empty();
        } else {
            return Mono.just(new ArrayList<>(map.keySet()));
        }
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        final Map<String, String> map = get0(key);
        map.put(value, String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        return Mono.just(template.set(CACHE_PREFIX + key, map, EXPIRATION_TIME));
    }

    @SuppressWarnings("ALL")
    private Map<String, String> get0(String key) {
        final Map map = template.get(CACHE_PREFIX + key, Map.class);
        if (map == null) {
            return new HashMap<>();
        } else {
            final Map<String, String> result = (Map<String, String>) map;
            final long now = System.currentTimeMillis();
            for (final String k : result.keySet()) {
                if (map.get(k) == null || now > Long.parseLong(result.get(k))) {
                    map.remove(k);
                }
            }
            return result;
        }
    }

}
