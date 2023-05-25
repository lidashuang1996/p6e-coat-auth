package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
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
 * OAUTH2 用户令牌信息缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2TokenUserclass,
//        ignored = Oauth2TokenUserAuthRedisCache.class
//)
//@ConditionalOnExpression(Oauth2TokenUserCONDITIONAL_EXPRESSION)
public class Oauth2TokenUserAuthRedisCache implements Oauth2TokenUserAuthCache {

    /**
     * 缓存源的名称
     */
    private static final String CACHE_SOURCE = "OAUTH2_TOKEN_USER_AUTH_SOURCE";

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;


    public Oauth2TokenUserAuthRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Token> set(String uid, String user, String scope, String accessToken, String refreshToken) {
        final Token token = new Token()
                .setUid(uid)
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
}
