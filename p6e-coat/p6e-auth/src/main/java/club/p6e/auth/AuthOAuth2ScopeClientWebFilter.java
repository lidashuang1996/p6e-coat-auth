package club.p6e.auth;

import club.p6e.auth.certificate.HttpCertificate;
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
public class AuthOAuth2ScopeClientWebFilter implements WebFilter, Ordered {

    /**
     * 匹配器对象
     */
    private final AuthOAuth2ScopeClientPathMatcher matcher;

    /**
     * 构造方法初始化
     *
     * @param matcher   匹配器对象
     */
    public AuthOAuth2ScopeClientWebFilter(AuthOAuth2ScopeClientPathMatcher matcher) {
        this.matcher = matcher;
    }

    @Override
    public @NonNull Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        if (matcher.match(exchange.getRequest().getPath().value())) {
            try {
                final String user = exchange
                        .getRequest()
                        .getQueryParams()
                        .getFirst(HttpCertificate.getUserInfoHeaderName());
                if (user != null) {

                }
            } catch (Exception e) {
                // ignore
            }
            return Mono.empty();
        } else {
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        return -500;
    }

}