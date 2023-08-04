package club.p6e.auth.cache.redis;

import club.p6e.auth.cache.redis.support.RedisCache;
import club.p6e.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.auth.utils.JsonUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenUserAuthRedisCache
        extends RedisCache implements Oauth2TokenUserAuthCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public Oauth2TokenUserAuthRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Token> set(String uid, String scope, String accessToken, String refreshToken, String user) {
        final Token token = new Token()
                .setUid(uid)
                .setScope(scope)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        final String json = JsonUtil.toJson(token);
        if (json == null) {
            return Mono.empty();
        }
        final byte[] jcBytes = json.getBytes(StandardCharsets.UTF_8);
        return template.execute(connection -> Flux.concat(
                connection.stringCommands().set(
                        ByteBuffer.wrap((USER_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                        ByteBuffer.wrap(user.getBytes(StandardCharsets.UTF_8)),
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                ),
                connection.stringCommands().set(
                        ByteBuffer.wrap((ACCESS_TOKEN_PREFIX + accessToken).getBytes(StandardCharsets.UTF_8)),
                        ByteBuffer.wrap(jcBytes),
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                ),
                connection.stringCommands().set(
                        ByteBuffer.wrap((REFRESH_TOKEN_PREFIX + refreshToken).getBytes(StandardCharsets.UTF_8)),
                        ByteBuffer.wrap(jcBytes),
                        Expiration.from(EXPIRATION_TIME, TimeUnit.SECONDS),
                        RedisStringCommands.SetOption.UPSERT
                )
        )).count().map(l -> token);
    }

    @Override
    public Mono<String> getUser(String uid) {
        return template
                .opsForValue()
                .get(USER_PREFIX + uid);
    }

    @Override
    public Mono<Token> getAccessToken(String content) {
        return template
                .opsForValue()
                .get(ACCESS_TOKEN_PREFIX + content)
                .flatMap(s -> {
                    final Token token = JsonUtil.fromJson(s, Token.class);
                    return token == null ? Mono.empty() : Mono.just(token);
                });
    }

    @Override
    public Mono<Token> getRefreshToken(String content) {
        return template
                .opsForValue()
                .get(REFRESH_TOKEN_PREFIX + content)
                .flatMap(s -> {
                    final Token token = JsonUtil.fromJson(s, Token.class);
                    return token == null ? Mono.empty() : Mono.just(token);
                });
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Long> cleanToken(String content) {
        return getAccessToken(content)
                .flatMap(t -> {
                    final String json = JsonUtil.toJson(t);
                    if (json == null) {
                        return Mono.empty();
                    }
                    return template.execute(connection -> Flux.concat(
                            connection.keyCommands().del(ByteBuffer.wrap(
                                    (ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8))),
                            connection.keyCommands().del(ByteBuffer.wrap(
                                    (REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8)))
                    )).count();
                });
    }

}
