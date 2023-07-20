package club.p6e.auth;

import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthWebFilter implements WebFilter, Ordered {

    private final AuthPathMatcher matcher;
    private final AuthCertificateValidator validator;

    public AuthWebFilter(AuthPathMatcher matcher, AuthCertificateValidator validator) {
        this.matcher = matcher;
        this.validator = validator;
    }

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        System.out.println("Before handling the request");
        System.out.println("Before handling the request");
        System.out.println("AuthWebFilter AuthWebFilter AuthWebFilter AuthWebFilter AuthWebFilter");
        if (matcher.match(exchange.getRequest().getPath().value())) {
            return validator
                    .execute(exchange)
                    .flatMap(chain::filter);
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -10000;
    }
}
