package club.p6e.auth.cache.redis;

import club.p6e.auth.cache.redis.support.RedisCache;
import club.p6e.auth.cache.AuthCache;
import club.p6e.auth.utils.JsonUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthRedisCache
        extends RedisCache implements AuthCache {

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
        final long now = System.currentTimeMillis();
        final byte[] jcBytes = json.getBytes(StandardCharsets.UTF_8);
        return init(uid)
                .flatMap(l -> template.execute(connection ->
                        Flux.concat(
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
                                connection.hashCommands().hSet(
                                        ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                                        ByteBuffer.wrap((ACCESS_TOKEN_PREFIX + accessToken).getBytes(StandardCharsets.UTF_8)),
                                        ByteBuffer.wrap((String.valueOf(now + EXPIRATION_TIME * 1000)).getBytes(StandardCharsets.UTF_8))
                                ),
                                connection.hashCommands().hSet(
                                        ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                                        ByteBuffer.wrap((REFRESH_TOKEN_PREFIX + refreshToken).getBytes(StandardCharsets.UTF_8)),
                                        ByteBuffer.wrap((String.valueOf(now + EXPIRATION_TIME * 1000)).getBytes(StandardCharsets.UTF_8))
                                )
                        )).count().map(r -> token)
                );
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

    @Override
    public Mono<Long> cleanToken(String content) {
        return getAccessToken(content)
                .flatMap(t -> template.execute(connection -> Flux.concat(
                        connection.keyCommands().del(ByteBuffer.wrap(
                                (ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8))),
                        connection.keyCommands().del(ByteBuffer.wrap(
                                (REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8))),
                        connection.hashCommands().hDel(ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8)),
                                ByteBuffer.wrap((ACCESS_TOKEN_PREFIX + t.getAccessToken()).getBytes(StandardCharsets.UTF_8))),
                        connection.hashCommands().hDel(ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + t.getUid()).getBytes(StandardCharsets.UTF_8)),
                                ByteBuffer.wrap((REFRESH_TOKEN_PREFIX + t.getRefreshToken()).getBytes(StandardCharsets.UTF_8)))
                )).count());
    }

    @Override
    public Mono<Long> cleanUserAll(String uid) {
        return getUser(uid)
                .flatMap(s -> template
                        .execute(connection -> Flux.concat(
                                connection.keyCommands().del(ByteBuffer.wrap(
                                        (USER_PREFIX + uid).getBytes(StandardCharsets.UTF_8))),
                                connection.hashCommands().hGetAll(ByteBuffer.wrap(
                                                (USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)))
                                        .collectList()
                                        .map(list -> {
                                            final Map<ByteBuffer, ByteBuffer> tmp = new HashMap<>(list.size());
                                            list.forEach(item -> tmp.put(item.getKey(), item.getValue()));
                                            return tmp;
                                        })
                                        .flatMap(m -> Flux.concat(m.keySet().stream().map(b ->
                                                Mono.defer(() -> connection.keyCommands().del(b))).toList().toArray(new Mono[0])).count())
                                        .flatMap(l -> connection.keyCommands().del(ByteBuffer.wrap(
                                                (USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8))))
                        )).count());
    }

    @SuppressWarnings("ALL")
    private Mono<Long> init(String uid) {
        return template
                .opsForHash()
                .entries(USER_PREFIX + uid)
                .collectList()
                .map(list -> {
                    final long now = System.currentTimeMillis();
                    final Map<String, String> map = new HashMap<>();
                    final Map<String, String> tmp = new HashMap<>(list.size());
                    list.forEach(item -> tmp.put((String) item.getKey(), (String) item.getValue()));
                    for (final String k : tmp.keySet()) {
                        if (map.get(k) == null || now > Long.parseLong(map.get(k))) {
                            map.put(k, map.get(k));
                        }
                    }
                    return map;
                })
                .flatMap(m -> template.execute(connection -> {
                    final List<Mono<Boolean>> mono = new ArrayList<>();
                    for (String key : m.keySet()) {
                        mono.add(connection.hashCommands().hDel(
                                ByteBuffer.wrap((USER_TOKEN_LIST_PREFIX + uid).getBytes(StandardCharsets.UTF_8)),
                                ByteBuffer.wrap(key.getBytes(StandardCharsets.UTF_8))
                        ));
                    }
                    return Flux.concat(mono.toArray(new Mono[0]));
                }).count());
    }

}
