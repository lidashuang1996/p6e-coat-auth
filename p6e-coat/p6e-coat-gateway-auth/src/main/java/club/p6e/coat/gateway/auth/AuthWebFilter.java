package club.p6e.coat.gateway.auth;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthWebFilter implements WebFilter {

    private final AuthPathMatcher matcher;
    private final AuthCertificateInterceptor interceptor;

    public AuthWebFilter(AuthPathMatcher matcher, AuthCertificateInterceptor interceptor) {
        this.matcher = matcher;
        this.interceptor = interceptor;
    }

    @Override
    public @NotNull Mono<Void> filter(ServerWebExchange exchange, @NotNull WebFilterChain chain) {
        if (matcher.match(exchange.getRequest().getPath().value())) {
            return interceptor
                    .execute(exchange)
                    .flatMap(chain::filter);
        } else {
            return chain.filter(exchange);
        }
    }

}
