package club.p6e.coat.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class GlobalHeaderInitFilter implements GlobalFilter {

    /**
     * 用户信息的头部名称
     */
    protected String content = "P6e-";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().headers(httpHeaders -> {
            for (final String key : httpHeaders.keySet()) {
                if (key.toLowerCase().startsWith(content.toLowerCase())) {
                    httpHeaders.remove(key);
                }
            }
        }).build()).build());
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
