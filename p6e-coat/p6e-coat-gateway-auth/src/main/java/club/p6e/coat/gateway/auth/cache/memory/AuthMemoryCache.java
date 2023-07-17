package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.cache.memory.support.MemoryCache;
import club.p6e.coat.gateway.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import io.r2dbc.postgresql.codec.Json;
import reactor.core.publisher.Mono;

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
        return null;
    }

    @Override
    public Mono<String> getUser(String uid) {
        final String r = template.get(USER_PREFIX + uid, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    public Mono<Token> getToken(String prefix, String content) {
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
        return null;
    }

    @Override
    public Mono<Long> cleaUserAll(String uid) {
        return null;
    }

    @Override
    public Mono<Long> cleanTokenAll(String content) {
        return null;
    }

}
