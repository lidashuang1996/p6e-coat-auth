package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 认证缓存服务
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AuthCache.class,
        ignored = AuthRedisCache.class
)
public class AuthRedisCache extends RedisCache implements AuthCache {

    /**
     * 缓存源的名称
     */
    private static final String CACHE_SOURCE = "AUTH_SOURCE";

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public AuthRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Token set(String uid, String user, String accessToken, String refreshToken) {
//        return cache.getStringRedisTemplate(CACHE_SOURCE).execute((RedisCallback<Token>) connection -> {
//            final Token token = new Token()
//                    .setUid(uid)
//                    .setAccessToken(accessToken)
//                    .setRefreshToken(refreshToken);
//            final byte[] tokenBytes = JsonUtil.toJson(token).getBytes(StandardCharsets.UTF_8);
//            connection.stringCommands().set((USER_PREFIX + uid).getBytes(StandardCharsets.UTF_8),
//                    user.getBytes(StandardCharsets.UTF_8),
//                    Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
//                    RedisStringCommands.SetOption.UPSERT);
//            connection.stringCommands().set((ACCESS_TOKEN_PREFIX + accessToken).getBytes(StandardCharsets.UTF_8),
//                    tokenBytes,
//                    Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
//                    RedisStringCommands.SetOption.UPSERT);
//            connection.stringCommands().set((REFRESH_TOKEN_PREFIX + refreshToken).getBytes(StandardCharsets.UTF_8),
//                    tokenBytes,
//                    Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
//                    RedisStringCommands.SetOption.UPSERT);
//            connection.listCommands().rPush(
//                    (USER_TOKEN_PREFIX + uid).getBytes(StandardCharsets.UTF_8),
//                    (ACCESS_TOKEN_PREFIX + accessToken).getBytes(StandardCharsets.UTF_8),
//                    (REFRESH_TOKEN_PREFIX + refreshToken).getBytes(StandardCharsets.UTF_8)
//            );
//            connection.keyCommands().expire((USER_TOKEN_PREFIX + uid).getBytes(StandardCharsets.UTF_8), EXPIRATION_TIME);
//            return token;
//        });
        return null;
    }

    @Override
    public Optional<String> get(String uid) {
//        return Optional.ofNullable(cache.getStringRedisTemplate(CACHE_SOURCE).opsForValue().get(USER_PREFIX + uid));
        return null;
    }

    @Override
    public Optional<Token> getAccessToken(String key) {
//        final String content = cache.getStringRedisTemplate(CACHE_SOURCE).opsForValue().get(ACCESS_TOKEN_PREFIX + key);
//        if (content != null) {
//            try {
//                return Optional.of(JsonUtil.fromJson(content, Token.class));
//            } catch (Exception e) {
//                // 忽略异常
//            }
//        }
//        return Optional.empty();
        return null;
    }

    @Override
    public Optional<Token> getRefreshToken(String key) {
//        final String content = cache.getStringRedisTemplate(CACHE_SOURCE).opsForValue().get(REFRESH_TOKEN_PREFIX + key);
//        if (content != null) {
//            try {
//                return Optional.of(JsonUtil.fromJson(content, Token.class));
//            } catch (Exception e) {
//                // 忽略异常
//            }
//        }
//        return Optional.empty();
        return null;
    }

    @Override
    public void cleanToken(String token) {
//        cache.getStringRedisTemplate(CACHE_SOURCE).execute((RedisCallback<Object>) connection -> {
//            final byte[] bytes = connection.stringCommands().get(
//                    (ACCESS_TOKEN_PREFIX + token).getBytes(StandardCharsets.UTF_8));
//            if (bytes != null && bytes.length > 0) {
//                final String content = new String(bytes, StandardCharsets.UTF_8);
//                final Token t = JsonUtil.fromJson(content, Token.class);
//                final byte[] abs = (ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8);
//                final byte[] rbs = (REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8);
//                connection.keyCommands().del(abs);
//                connection.keyCommands().del(rbs);
//                connection.listCommands().lRem(
//                        (USER_TOKEN_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8), 0, abs);
//                connection.listCommands().lRem(
//                        (USER_TOKEN_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8), 0, rbs);
//            }
//            return null;
//        });
    }

    @Override
    public void cleanTokenAll(String token) {
//        template.getStringRedisTemplate(CACHE_SOURCE).execute((RedisCallback<Object>) connection -> {
//            final byte[] bytes = connection.stringCommands().get(
//                    (ACCESS_TOKEN_PREFIX + token).getBytes(StandardCharsets.UTF_8));
//            if (bytes != null && bytes.length > 0) {
//                final String content = new String(bytes, StandardCharsets.UTF_8);
//                final Token t = JsonUtil.fromJson(content, Token.class);
//                final List<byte[]> ls = connection.listCommands().lRange(
//                        (USER_TOKEN_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8), 0, -1);
//                if (ls != null && ls.size() > 0) {
//                    for (byte[] l : ls) {
//                        connection.keyCommands().del(l);
//                    }
//                }
//                connection.keyCommands().del((USER_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8));
//                connection.keyCommands().del((USER_TOKEN_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8));
//            }
//            return null;
//        });
    }

}
