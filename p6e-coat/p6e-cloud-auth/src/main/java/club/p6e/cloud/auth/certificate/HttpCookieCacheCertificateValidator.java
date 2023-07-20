package club.p6e.cloud.auth.certificate;

import club.p6e.cloud.auth.AuthCertificateValidator;
import club.p6e.cloud.auth.cache.AuthCache;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 认证凭证拦截验证（HttpCookieCache）
 *
 * @author lidashuang
 * @version 1.0
 */
public class HttpCookieCacheCertificateValidator
        extends HttpCertificate implements AuthCertificateValidator {

    /**
     * 认证缓存的对象
     */
    protected final AuthCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 认证缓存的对象
     */
    public HttpCookieCacheCertificateValidator(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpCookieToken(exchange.getRequest())
                .flatMap(this::accessToken)
                .map(r -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(USER_HEADER_NAME, r).build()
                ).build());
    }

    @Override
    public Mono<String> accessToken(String token) {
        return cache.getAccessToken(token)
                .flatMap(t -> cache.getUser(t.getUid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun accessToken(String token)",
                        "[HTTP/COOKIE] Verifier validation access token exception."
                )));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return cache.getRefreshToken(token)
                .flatMap(t -> cache.getUser(t.getUid()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun refreshToken(String token)",
                        "[HTTP/COOKIE] Verifier validation refresh token exception."
                )));
    }
}
