package club.p6e.coat.gateway.filter;

import club.p6e.coat.gateway.WebFilterOrder;
import org.reactivestreams.Publisher;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

/**
 * Multiple Cross Domain 过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class MultipleCrossDomainWebFilter implements WebFilter, Ordered {

    /**
     * 执行顺序
     */
    protected static final WebFilterOrder ORDER = WebFilterOrder.MULTIPLE_CROSS_DOMAIN_FILTER;

    /**
     * Cross Domain 请求头前缀
     */
    protected static final String ACCESS_CONTROL_PREFIX = "Access-Control";

    @Override
    public int getOrder() {
        return ORDER.getOrder();
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        final ServerHttpResponse response = exchange.getResponse();
        final ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(response) {
            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                final HttpHeaders httpHeaders = response.getHeaders();
                final Set<String> httpHeaderNames = httpHeaders.keySet();
                for (final String httpHeaderName : httpHeaderNames) {
                    if (httpHeaderName.startsWith(ACCESS_CONTROL_PREFIX)) {
                        final List<String> httpHeaderValue = httpHeaders.get(httpHeaderName);
                        if (httpHeaderValue != null && httpHeaderValue.size() > 0) {
                            httpHeaders.set(httpHeaderName, httpHeaderValue.get(0));
                        } else {
                            httpHeaders.remove(httpHeaderName);
                        }
                    }
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }

}
