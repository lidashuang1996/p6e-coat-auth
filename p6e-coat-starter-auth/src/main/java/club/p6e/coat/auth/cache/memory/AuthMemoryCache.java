package club.p6e.coat.auth.cache.memory;

import club.p6e.coat.auth.utils.JsonUtil;
import club.p6e.coat.auth.cache.AuthCache;
import club.p6e.coat.auth.cache.memory.support.MemoryCache;
import club.p6e.coat.auth.cache.memory.support.ReactiveMemoryTemplate;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthMemoryCache extends MemoryCache implements AuthCache {

    /**
     * 内存缓存模板对象
     */
    private final ReactiveMemoryTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 内存缓存模板对象
     */
    public AuthMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Token> set(String uid, String device, String accessToken, String refreshToken, String user) {
        final Token token = new Token()
                .setUid(uid)
                .setDevice(device)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        final String json = JsonUtil.toJson(token);
        if (json == null) {
            return Mono.empty();
        }
        final Map<String, String> map = get0(uid);
        map.put(ACCESS_TOKEN_PREFIX + accessToken,
                String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        map.put(REFRESH_TOKEN_PREFIX + refreshToken,
                String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        template.set(USER_PREFIX + uid, user, EXPIRATION_TIME);
        template.set(USER_TOKEN_LIST_PREFIX + uid, map, EXPIRATION_TIME);
        template.set(ACCESS_TOKEN_PREFIX + accessToken, json, EXPIRATION_TIME);
        template.set(REFRESH_TOKEN_PREFIX + refreshToken, json, EXPIRATION_TIME);
        return Mono.just(token);
    }

    @Override
    public Mono<String> getUser(String uid) {
        final String r = template.get(USER_PREFIX + uid, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    @SuppressWarnings("ALL")
    private Mono<Token> getToken(String prefix, String content) {
        final String r = template.get(prefix + content, String.class);
        if (r == null) {
            return Mono.empty();
        } else {
            final Token token = JsonUtil.fromJson(r, Token.class);
            return token == null ? Mono.empty() : Mono.just(token);
        }
    }

    @Override
    public Mono<Token> getAccessToken(String content) {
        return getToken(ACCESS_TOKEN_PREFIX, content);
    }

    @Override
    public Mono<Token> getRefreshToken(String content) {
        return getToken(REFRESH_TOKEN_PREFIX, content);
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Long> cleanToken(String content) {
        final String r = template.get(ACCESS_TOKEN_PREFIX + content, String.class);
        if (r != null) {
            final Token token = JsonUtil.fromJson(r, Token.class);
            if (token != null) {
                template.del(ACCESS_TOKEN_PREFIX + token.getAccessToken());
                template.del(REFRESH_TOKEN_PREFIX + token.getRefreshToken());
                return Mono.just(1L);
            }
        }
        return Mono.just(0L);
    }

    @Override
    public Mono<Long> cleanUserAll(String uid) {
        final String r = template.get(USER_PREFIX + uid, String.class);
        if (r != null) {
            final Map<String, String> map = get0(uid);
            for (final String key : map.keySet()) {
                template.del(key);
            }
            template.del(USER_PREFIX + uid);
            template.del(USER_TOKEN_LIST_PREFIX + uid);
        }
        return Mono.just(0L);
    }

    @SuppressWarnings("ALL")
    private Map<String, String> get0(String uid) {
        final Map map = template.get(USER_TOKEN_LIST_PREFIX + uid, Map.class);
        if (map == null) {
            return new HashMap<>();
        } else {
            final long now = System.currentTimeMillis();
            final Map<String, String> result = new HashMap<>();
            final Map<String, String> data = (Map<String, String>) map;
            for (final String key : data.keySet()) {
                final String value = data.get(key);
                if (now <= Long.parseLong(value)) {
                    result.put(key, data.get(key));
                }
            }
            return result;
        }
    }

}
