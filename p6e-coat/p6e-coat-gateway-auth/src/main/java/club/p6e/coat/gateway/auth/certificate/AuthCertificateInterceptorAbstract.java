package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateInterceptorAbstract implements AuthCertificateInterceptor {

    /**
     * 用户信息的头部名称
     */
    protected static final String P6E_HEADER_PREFIX_NAME = "P6e-";

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return Mono.just(exchange.mutate().request(
                exchange.getRequest().mutate().headers(httpHeaders -> {
                    for (final String key : httpHeaders.keySet()) {
                        if (key.equalsIgnoreCase(P6E_HEADER_PREFIX_NAME)) {
                            httpHeaders.remove(key);
                        }
                    }
                }).build()
        ).build());
    }

}
