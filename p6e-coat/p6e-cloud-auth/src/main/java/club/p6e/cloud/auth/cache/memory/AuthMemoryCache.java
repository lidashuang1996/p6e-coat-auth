package club.p6e.cloud.auth.cache.memory;

import club.p6e.cloud.auth.cache.AuthCache;
import club.p6e.cloud.auth.cache.memory.support.MemoryCache;
import club.p6e.cloud.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.cloud.auth.utils.JsonUtil;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthMemoryCache extends MemoryCache implements AuthCache {

    private final ReactiveMemoryTemplate template;

    public AuthMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Token> set(String uid, String device, String accessToken, String refreshToken, String user) {
        // 在这里可以考虑实现控制设备登录的限制
        // 或者实现用户最多同时几个会话在使用
        // 这里配置的是无限多个（很多的时候会崩）
        // 构造缓存的数据对象
        final Token token = new Token()
                .setUid(uid)
                .setDevice(device)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        // 序列化为字符串方便存储
        final String jc = JsonUtil.toJson(token);
        // 读取之前缓存的令牌数据（主要是删除过期的令牌的数据）
        final Map<String, String> map = get0(uid);
        map.put(ACCESS_TOKEN_PREFIX + accessToken,
                String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        map.put(REFRESH_TOKEN_PREFIX + refreshToken,
                String.valueOf(System.currentTimeMillis() + EXPIRATION_TIME * 1000));
        // 写入此次操作的缓存数据
        template.set(USER_PREFIX + uid, user);
        template.set(USER_TOKEN_LIST_PREFIX + uid, map, EXPIRATION_TIME);
        template.set(ACCESS_TOKEN_PREFIX + accessToken, jc, EXPIRATION_TIME);
        template.set(REFRESH_TOKEN_PREFIX + refreshToken, jc, EXPIRATION_TIME);
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

    @Override
    public Mono<Long> cleanToken(String content) {
        final String r = template.get(ACCESS_TOKEN_PREFIX + content, String.class);
        if (r != null) {
            try {
                final Token token = JsonUtil.fromJson(r, Token.class);
                if (token != null) {
                    template.del(ACCESS_TOKEN_PREFIX + token.getAccessToken());
                    template.del(REFRESH_TOKEN_PREFIX + token.getRefreshToken());
                    return Mono.just(1L);
                }
            } catch (Exception e) {
                // ignore exceptions
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
