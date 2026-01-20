package club.p6e.coat.auth.certificate;

import club.p6e.coat.auth.AuthCertificateValidator;
import club.p6e.coat.auth.AuthParamHandler;
import club.p6e.coat.auth.cache.AuthCache;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import club.p6e.coat.common.utils.SpringUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * [ HTTP/STORAGE/CACHE ] CertificateValidator
 *
 * @author lidashuang
 * @version 1.0
 */
@SuppressWarnings("ALL")
public class HttpLocalStorageCacheCertificateValidator extends HttpCertificate implements AuthCertificateValidator {

    /**
     * 认证缓存的对象
     */
    protected final AuthCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存的对象
     */
    public HttpLocalStorageCacheCertificateValidator(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpLocalStorageToken(exchange.getRequest())
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun execute(ServerWebExchange exchange)",
                        "[HTTP/STORAGE/CACHE] HTTP request access token does not exist."
                )))
                .flatMap(t -> accessToken(t, exchange))
                .map(s -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(getUserInfoHeaderName(), s).build()
                ).build());
    }

    @Override
    public Mono<String> accessToken(String token, ServerWebExchange exchange) {
        final AuthParamHandler authParamHandler = SpringUtil.getBean(AuthParamHandler.class);
        return authParamHandler.execute(exchange)
                .flatMap(r -> cache.getAccessToken(token, r).flatMap(t -> cache.getUser(t.getUid(), r)))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun accessToken(String token).",
                        "[HTTP/STORAGE/CACHE] Verifier validation access token exception."
                )));
    }

    @Override
    public Mono<String> refreshToken(String token, ServerWebExchange exchange) {
        final AuthParamHandler authParamHandler = SpringUtil.getBean(AuthParamHandler.class);
        return authParamHandler.execute(exchange)
                .flatMap(r -> cache.getRefreshToken(token, r).flatMap(t -> cache.getUser(t.getUid(), r)))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun refreshToken(String token).",
                        "[HTTP/STORAGE/CACHE] Verifier validation refresh token exception."
                )));
    }
}
