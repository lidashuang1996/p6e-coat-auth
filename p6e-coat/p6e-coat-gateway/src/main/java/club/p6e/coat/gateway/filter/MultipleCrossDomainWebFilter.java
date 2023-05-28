package club.p6e.coat.gateway.filter;

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
 * 自定义返回头清除过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class MultipleCrossDomainWebFilter implements WebFilter, Ordered {

    /**
     * 执行顺序
     */
    private static final int ORDER = -2900;

    /**
     * 替换删除的请求头
     */
    private static final String ACCESS_CONTROL_PREFIX = "Access-Control";

    @Override
    public int getOrder() {
        return ORDER;
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
                for (String httpHeaderName : httpHeaderNames) {
                    final List<String> httpHeaderValue = httpHeaders.get(httpHeaderName);
                    // 主要是消除多次跨域的问题
                    if (httpHeaderValue != null
                            && httpHeaderValue.size() > 0
                            && httpHeaderName.startsWith(ACCESS_CONTROL_PREFIX)) {
                        httpHeaders.set(httpHeaderName, httpHeaderValue.get(0));
                    }
                }
                return super.writeWith(body);
            }
        };
        return chain.filter(exchange.mutate().response(responseDecorator).build());
    }
}
