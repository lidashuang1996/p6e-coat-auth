package club.p6e.auth.cache.memory;

import club.p6e.auth.cache.Oauth2TokenClientAuthCache;
import club.p6e.auth.cache.memory.support.MemoryCache;
import club.p6e.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.auth.utils.JsonUtil;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2TokenClientAuthMemoryCache extends MemoryCache implements Oauth2TokenClientAuthCache {

    private final ReactiveMemoryTemplate template;

    public Oauth2TokenClientAuthMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Token> set(String cid, String scope, String accessToken, String refreshToken, String client) {
        final Token token = new Token()
                .setCid(cid)
                .setScope(scope)
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken);
        // 序列化为字符串方便存储
        final String jc = JsonUtil.toJson(token);
        // 写入此次操作的缓存数据
        template.set(CLIENT_PREFIX + cid, client);
        template.set(ACCESS_TOKEN_PREFIX + accessToken, jc, EXPIRATION_TIME);
        template.set(REFRESH_TOKEN_PREFIX + refreshToken, jc, EXPIRATION_TIME);
        return Mono.just(token);
    }

    @Override
    public Mono<String> getClient(String cid) {
        final String r = template.get(CLIENT_PREFIX + cid, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    @SuppressWarnings("ALL")
    private Mono<Token> getToken(String prefix, String content) {
        final String r = template.get(prefix + content, String.class);
        if (r == null) {
            return Mono.empty();
        } else {
            final Token token = JsonUtil.fromJson(r, Token.class);
            return token == null ? Mono.empty() : Mono.just(token);
        }
    }

    @Override
    public Mono<Token> getAccessToken(String token) {
        return getToken(ACCESS_TOKEN_PREFIX, token);
    }

    @Override
    public Mono<Token> getRefreshToken(String token) {
        return getToken(REFRESH_TOKEN_PREFIX, token);
    }

    @SuppressWarnings("ALL")
    @Override
    public Mono<Long> cleanToken(String content) {
        final String r = template.get(ACCESS_TOKEN_PREFIX + content, String.class);
        if (r != null) {
            try {
                final Token token = JsonUtil.fromJson(r, Token.class);
                if (token != null) {
                    template.del(ACCESS_TOKEN_PREFIX + token.getAccessToken());
                    template.del(REFRESH_TOKEN_PREFIX + token.getRefreshToken());
                    return Mono.just(1L);
                }
            } catch (Exception e) {
                // ignore exceptions
            }
        }
        return Mono.just(0L);
    }

}
