package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateInterceptorHttpLocalStorageCache
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
    public AuthCertificateInterceptorHttpLocalStorageCache(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        String accessToken = null;
        final ServerHttpRequest request = exchange.getRequest();
        final List<String> authorizations = request.getHeaders().get(AUTH_HEADER);
        if (authorizations != null && authorizations.size() > 0) {
            final String authorization = authorizations.get(0);
            if (StringUtils.hasText(authorization)
                    && authorization.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
                accessToken = authorization.substring(AUTH_HEADER_TOKEN_PREFIX.length());
            }
        }
        if (accessToken == null) {
            accessToken = getParamValue(request);
        }
        if (accessToken == null) {
            return Mono.error(GlobalExceptionContext.exceptionAuthException(
                    this.getClass(),
                    "fun execute(ServerWebExchange exchange)",
                    "Auth exception. [No authentication token found]"
            ));
        } else {
            return cache
                    .getAccessToken(accessToken)
                    .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                            this.getClass(),
                            "fun execute(ServerWebExchange exchange)",
                            "Auth exception. [Error reading authentication token cache]")
                    ))
                    .flatMap(t -> cache
                            .getUser(t.getUid())
                            .map(u -> exchange.mutate().request(request.mutate().header(USER_HEADER_NAME, u).build()).build())
                    );
        }
    }
}
