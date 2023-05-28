package club.p6e.coat.gateway.filter;

import club.p6e.coat.gateway.Properties;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 自定义 REFERER 滤器服务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class RefererWebFilter implements WebFilter, Ordered {

    /**
     * 执行顺序
     */
    private static final int ORDER = -2600;

    /**
     * REFERER
     */
    private static final String REFERER_HEADER = "Referer";

    /**
     * REFERER 通用内容
     */
    private static final String REFERER_HEADER_GENERAL_CONTENT = "*";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public RefererWebFilter(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int getOrder() {
        return ORDER;
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        if (!properties.getReferer().isEnabled()) {
            return chain.filter(exchange);
        }
        final ServerHttpRequest request = exchange.getRequest();
        final List<String> refererList = request.getHeaders().get(REFERER_HEADER);
        if (refererList != null && refererList.size() > 0) {
            final String r = refererList.get(0);
            final String referer = r == null ? "" : r;
            for (final String w : properties.getReferer().getWhiteList()) {
                if (REFERER_HEADER_GENERAL_CONTENT.equalsIgnoreCase(w) || referer.startsWith(w)) {
                    return chain.filter(exchange);
                }
            }
        } else {
            for (final String w : properties.getReferer().getWhiteList()) {
                if (REFERER_HEADER_GENERAL_CONTENT.equalsIgnoreCase(w)) {
                    return chain.filter(exchange);
                }
            }
        }
        return Mono.empty();
    }
}
