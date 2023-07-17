package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.coat.gateway.auth.cache.memory.support.MemoryCache;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenClientAuthMemoryCache extends MemoryCache implements Oauth2TokenClientAuthCache {
    @Override
    public Mono<Token> set(String cid, String scope, String accessToken, String refreshToken, String client) {
        return null;
    }

    @Override
    public Mono<String> getClient(String cid) {
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
