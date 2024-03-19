package club.p6e.coat.auth.client.cache.memory;

import club.p6e.coat.auth.client.cache.AuthCacheReactive;
import club.p6e.coat.auth.client.cache.memory.support.MemoryCache;
import club.p6e.coat.auth.client.cache.memory.support.MemoryTemplate;
import club.p6e.coat.common.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnClass(name = "org.springframework.web.reactive.package-info")
@ConditionalOnProperty(name = "club.p6e.coat.auth.client.cache.type", havingValue = "MEMORY")
public class AuthMemoryCacheReactive extends MemoryCache implements AuthCacheReactive {

    /**
     * 内存缓存模板对象
     */
    private final MemoryTemplate template;

    /**
     * 构造方法初始化
     *
     * @param template 内存缓存模板对象
     */
    public AuthMemoryCacheReactive(MemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Token> set(String uid, String device, String accessToken, String refreshToken, String user) {
        final Token token = new Token().setUid(uid).setDevice(device)
                .setAccessToken(accessToken).setRefreshToken(refreshToken);
        final String json = JsonUtil.toJson(token);
        if (json == null) {
            return Mono.empty();
        } else {
            template.set(REFRESH_TOKEN_PREFIX + refreshToken, json, EXPIRATION_TIME);
            template.set(ACCESS_TOKEN_PREFIX + accessToken, json, EXPIRATION_TIME);
            template.set(USER_REFRESH_TOKEN_PREFIX + uid + DELIMITER + refreshToken, json, EXPIRATION_TIME);
            template.set(USER_ACCESS_TOKEN_PREFIX + uid + DELIMITER + accessToken, json, EXPIRATION_TIME);
            template.set(USER_PREFIX + uid, json, EXPIRATION_TIME);
            return Mono.just(token);
        }
    }

}
