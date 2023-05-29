package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.gateway.auth.cache.redis.support.RedisCache;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * OAUTH2 客户端令牌信息缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2TokenClientclass,
//        ignored = Oauth2TokenClientAuthRedisCache.class
//)
//@ConditionalOnExpression(Oauth2TokenClientCONDITIONAL_EXPRESSION)
public class Oauth2TokenClientAuthRedisCache extends RedisCache implements Oauth2TokenClientAuthCache {

    /**
     * 缓存源的名称
     */
    private static final String CACHE_SOURCE = "OAUTH2_TOKEN_CLIENT_AUTH_SOURCE";

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;


    public Oauth2TokenClientAuthRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }


    @Override
    public Mono<Token> set(String cid, String client, String scope, String accessToken, String refreshToken) {
        final Token token = new Token()
                .setCid(cid)
                .setScope(scope)
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
                        ByteBuffer.wrap((CLIENT_PREFIX + cid).getBytes(StandardCharsets.UTF_8)),
                        ByteBuffer.wrap(client.getBytes(StandardCharsets.UTF_8)),
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
                        ByteBuffer.wrap((CLIENT_TOKEN_LIST_PREFIX + cid).getBytes(StandardCharsets.UTF_8)),
                        List.of(ByteBuffer.wrap(jcBytes))
                ).map(l -> l > 0)
        )).collectList().map(l -> token);
    }

    @Override
    public Mono<String> getClient(String uid) {
        return template
                .opsForValue()
                .get(ByteBuffer.wrap((CLIENT_PREFIX + uid).getBytes(StandardCharsets.UTF_8)));
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
                                                            ByteBuffer.wrap((CLIENT_TOKEN_LIST_PREFIX
                                                                    + t.getCid()).getBytes(StandardCharsets.UTF_8)),
                                                            1L,
                                                            ByteBuffer.wrap(jcBytes)
                                                    ))
                            )
                            .collectList()
                            .map(l -> l.size() / 3L);
                });
    }
}
