package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 外交部抽象类
 * 通过 HTTP -> COOKIE 的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateInterceptorHttpCookieCache
        extends AuthCertificateInterceptorBaseHttp implements AuthCertificateInterceptor {

    /**
     * 认证缓存的对象
     */
    protected final AuthCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存的对象
     */
    public AuthCertificateInterceptorHttpCookieCache(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpCookieAccessToken(exchange.getRequest())
                .flatMap(cache::getAccessToken)
                .flatMap(t -> cache.getUser(t.getUid()))
                .map(s -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(USER_HEADER_NAME, s).build()
                ).build());
    }

}
