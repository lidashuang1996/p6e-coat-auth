package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.gateway.auth.cache.Oauth2TokenUserAuthCache;
import club.p6e.coat.gateway.auth.cache.memory.support.MemoryCache;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenUserAuthMemoryCache extends MemoryCache implements Oauth2TokenUserAuthCache {
    @Override
    public Mono<Token> set(String uid, String scope, String accessToken, String refreshToken, String user) {
        return null;
    }

    @Override
    public Mono<String> getUser(String uid) {
        return null;
    }

    @Override
    public Mono<Token> getAccessToken(String token) {
        return null;
    }

    @Override
    public Mono<Token> getRefreshToken(String token) {
        return null;
    }

    @Override
    public Mono<Long> cleanToken(String token) {
        return null;
    }
}
