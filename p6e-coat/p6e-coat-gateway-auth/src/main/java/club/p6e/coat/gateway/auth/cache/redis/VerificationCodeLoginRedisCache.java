package club.p6e.coat.gateway.auth.cache.redis;

import club.p6e.coat.gateway.auth.cache.VerificationCodeLoginCache;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 验证码登录的缓存
 * 采用 REDIS 实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class VerificationCodeLoginRedisCache implements VerificationCodeLoginCache {

    /**
     * 缓存对象
     */
    private final ReactiveStringRedisTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 缓存对象
     */
    public VerificationCodeLoginRedisCache(ReactiveStringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key, String type) {
        return template.delete(CACHE_PREFIX + key + DELIMITER + type);
    }

    @Override
    public Mono<Long> del(String key, String type, String value) {
        final byte[] bytes = (CACHE_PREFIX + key + DELIMITER + type).getBytes(StandardCharsets.UTF_8);
        return template
                .execute(connection -> connection
                        .hashCommands()
                        .hGetAll(ByteBuffer.wrap(bytes))
                        .collectList()
                        .map(l -> {
                            final List<String> dl = new ArrayList<>();
                            if (l.size() > 0) {
                                final long current = System.currentTimeMillis();
                                for (Map.Entry<ByteBuffer, ByteBuffer> m : l) {
                                    final String k = new String(m.getKey().array(), StandardCharsets.UTF_8);
                                    if (k.equals(value)
                                            || current >= Long.parseLong(new String(
                                            m.getValue().array(), StandardCharsets.UTF_8))) {
                                        dl.add(k);
                                    }
                                }
                            }
                            return dl;
                        })
                        .flatMap(l -> connection
                                .hashCommands()
                                .hDel(ByteBuffer.wrap(bytes), l.stream().map(s ->
                                        ByteBuffer.wrap(s.getBytes(StandardCharsets.UTF_8))).toList())))
                .collectList().map(l -> (long) l.size());
    }

    @Override
    public Mono<Boolean> set(String key, String type, String value) {
        final byte[] bytes = (CACHE_PREFIX + key + DELIMITER + type).getBytes(StandardCharsets.UTF_8);
        return template
                .execute(connection -> connection
                        .hashCommands()
                        .hSet(
                                ByteBuffer.wrap(bytes),
                                ByteBuffer.wrap(value.getBytes(StandardCharsets.UTF_8)),
                                ByteBuffer.wrap(String.valueOf(System.currentTimeMillis()
                                        + EXPIRATION_TIME * 1000).getBytes(StandardCharsets.UTF_8))
                        )
                        .flatMap(b -> connection.keyCommands().expire(ByteBuffer.wrap(bytes), Duration.of(EXPIRATION_TIME, ChronoUnit.SECONDS)))
                ).collectList().map(l -> l.size() > 0);
    }

    @Override
    public Mono<List<String>> get(String key, String type) {
        final byte[] bytes = (CACHE_PREFIX + key + DELIMITER + type).getBytes(StandardCharsets.UTF_8);
        return template
                .execute(connection -> connection
                        .hashCommands()
                        .hGetAll(ByteBuffer.wrap(bytes))
                        .flatMap(e -> {
                            final String key1 = new String(e.getKey().array(), StandardCharsets.UTF_8);
                            final String value = new String(e.getValue().array(), StandardCharsets.UTF_8);
                            return System.currentTimeMillis() >= Long.parseLong(value) ? Mono.defer(() -> connection
                                    .hashCommands()
                                    .hDel(ByteBuffer.wrap(bytes), e.getKey())
                                    .map(b -> "")
                            ) : Mono.just(key1);
                        }))
                .collectList()
                .map(l -> l.stream().filter(StringUtils::hasText).toList());
    }

}
