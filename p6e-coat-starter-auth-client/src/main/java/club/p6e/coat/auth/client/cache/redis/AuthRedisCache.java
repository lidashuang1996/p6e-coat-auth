package club.p6e.coat.auth.client.cache.redis;

import club.p6e.coat.auth.client.cache.AuthCache;
import club.p6e.coat.auth.client.cache.redis.support.RedisCache;
import club.p6e.coat.common.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnClass(name = "org.springframework.web.servlet.package-info")
@ConditionalOnProperty(name = "club.p6e.coat.auth.client.cache.type", havingValue = "REDIS")
public class AuthRedisCache extends RedisCache implements AuthCache {

    /**
     * 缓存对象
     */
    private final StringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public AuthRedisCache(StringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Token set(String uid, String device, String accessToken, String refreshToken, String user) {
        final Token token = new Token().setUid(uid).setDevice(device)
                .setAccessToken(accessToken).setRefreshToken(refreshToken);
        final String json = JsonUtil.toJson(token);
        if (json == null) {
            return null;
        } else {
            final byte[] jcBytes = json.getBytes(StandardCharsets.UTF_8);
            return template.execute((RedisCallback<Token>) connection -> {
                connection.commands().set(
                        (USER_PREFIX + uid).getBytes(StandardCharsets.UTF_8),
                        user.getBytes(StandardCharsets.UTF_8),
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                );
                connection.stringCommands().set(
                        (ACCESS_TOKEN_PREFIX + accessToken).getBytes(StandardCharsets.UTF_8),
                        jcBytes,
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                );
                connection.stringCommands().set(
                        (REFRESH_TOKEN_PREFIX + refreshToken).getBytes(StandardCharsets.UTF_8),
                        jcBytes,
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                );
                connection.stringCommands().set(
                        (USER_ACCESS_TOKEN_PREFIX + uid + DELIMITER + accessToken).getBytes(StandardCharsets.UTF_8),
                        jcBytes,
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                );
                connection.stringCommands().set(
                        (USER_REFRESH_TOKEN_PREFIX + uid + DELIMITER + refreshToken).getBytes(StandardCharsets.UTF_8),
                        jcBytes,
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                );
                return null;
            });
        }
    }

}
