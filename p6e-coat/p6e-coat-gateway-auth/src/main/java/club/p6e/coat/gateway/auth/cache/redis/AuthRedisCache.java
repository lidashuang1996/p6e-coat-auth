package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.cache.redis.support.RedisCache;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
    public Mono<Token> set(String uid, String device, String accessToken, String refreshToken, String user) {
        final Token token = new Token()
                .setUid(uid)
                .setDevice(device)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        final String jc = JsonUtil.toJson(token);
        System.out.println(token);
        System.out.println(jc);
        if (jc == null) {
            throw new RuntimeException();
        }
        final byte[] jcBytes = jc.getBytes(StandardCharsets.UTF_8);
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
                ),
                connection.listCommands().rPush(
                        ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                        List.of(ByteBuffer.wrap(jcBytes))
                ).map(l -> l > 0)
        )).collectList().map(l -> token);
    }

    @Override
    public Mono<String> getUser(String uid) {
        return template
                .opsForValue()
                .get(ByteBuffer.wrap((USER_PREFIX + uid).getBytes(StandardCharsets.UTF_8)));
    }

    @Override
    public Mono<Token> getAccessToken(String content) {
        return template
                .opsForValue()
                .get(ByteBuffer.wrap((ACCESS_TOKEN_PREFIX + content).getBytes(StandardCharsets.UTF_8)))
                .flatMap(s -> {
                    final Token token = JsonUtil.fromJson(s, Token.class);
                    return token == null ? Mono.empty() : Mono.just(token);
                });
    }

    @Override
    public Mono<Token> getRefreshToken(String content) {
        return template
                .opsForValue()
                .get(ByteBuffer.wrap((REFRESH_TOKEN_PREFIX + content).getBytes(StandardCharsets.UTF_8)))
                .flatMap(s -> {
                    final Token token = JsonUtil.fromJson(s, Token.class);
                    return token == null ? Mono.empty() : Mono.just(token);
                });
    }

    @Override
    public Mono<Long> cleanToken(String content) {
        return getAccessToken(content)
                .flatMap(t -> {
                    final String jc = JsonUtil.toJson(t);
                    if (jc == null) {
                        return Mono.error(new RuntimeException());
                    }
                    final byte[] jcBytes = jc.getBytes(StandardCharsets.UTF_8);
                    return template
                            .execute(
                                    connection -> Flux.concat(
                                            connection.keyCommands().del(ByteBuffer.wrap(
                                                    (ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8))),
                                            connection.keyCommands().del(ByteBuffer.wrap(
                                                    (REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8))),
                                            connection
                                                    .listCommands()
                                                    .lRem(
                                                            ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX
                                                                    + t.getUid()).getBytes(StandardCharsets.UTF_8)),
                                                            1L,
                                                            ByteBuffer.wrap(jcBytes)
                                                    ))
                            )
                            .collectList()
                            .map(l -> l.size() / 3L);
                });
    }

    @Override
    public Mono<Long> cleaUserAll(String uid) {
        return getUser(uid)
                .flatMap(s -> template
                        .execute(
                                connection -> connection
                                        .listCommands()
                                        .lRange(
                                                ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                                                0,
                                                -1
                                        )
                                        .flatMap(b -> {
                                            final Token token = JsonUtil.fromJson(new String(b.array()), Token.class);
                                            return connection
                                                    .keyCommands()
                                                    .del(b)
                                                    .map(l -> token == null ? new Token() : token);
                                        })
                                        .flatMap(t -> {
                                            if (t != null && t.getAccessToken() != null) {
                                                return Flux
                                                        .concat(
                                                                connection.keyCommands().del(ByteBuffer.wrap(
                                                                        (ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8))),
                                                                connection.keyCommands().del(ByteBuffer.wrap(
                                                                        (REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8)))
                                                        )
                                                        .collectList()
                                                        .map(l -> l.size() / 2L);
                                            } else {
                                                return Mono.just(0L);
                                            }
                                        })
                        )
                        .collectList()
                        .map(l -> l.stream().reduce(0L, Long::sum))
                );
    }

    @Override
    public Mono<Long> cleanTokenAll(String content) {
        return getAccessToken(content)
                .flatMap(t -> cleaUserAll(t.getUid()));
    }

}
