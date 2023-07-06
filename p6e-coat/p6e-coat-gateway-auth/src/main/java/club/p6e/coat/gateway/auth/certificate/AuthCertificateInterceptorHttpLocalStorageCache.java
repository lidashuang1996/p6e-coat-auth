package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateValidator;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateInterceptorHttpLocalStorageCache
        extends AuthCertificateHttp implements AuthCertificateValidator {

    /**
     * 认证缓存的对象
     */
    protected final AuthCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存的对象
     */
    public AuthCertificateInterceptorHttpLocalStorageCache(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpLocalStorageAccessToken(exchange.getRequest())
                .flatMap(this::accessToken)
                .map(s -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(USER_HEADER_NAME, s).build()
                ).build());
    }

    @Override
    public Mono<String> accessToken(String token) {
        return cache.getAccessToken(token)
                .flatMap(t -> cache.getUser(t.getUid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "",
                        ""
                )));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return cache.getRefreshToken(token)
                .flatMap(t -> cache.getUser(t.getUid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "",
                        ""
                )));
    }
}
