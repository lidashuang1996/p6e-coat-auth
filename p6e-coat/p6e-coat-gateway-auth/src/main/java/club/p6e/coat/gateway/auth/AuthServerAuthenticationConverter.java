package club.p6e.coat.gateway.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthServerAuthenticationConverter extends ServerAuthenticationConverter {

    /**
     * 清理的头部信息的前缀
     */
    String CLEANED_HEADER_NAME_PREFIX = "P6e-";

    default ServerWebExchange initServerWebExchange(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        exchange.mutate().request(request.mutate().headers(hh -> {
            for (final String key : hh.keySet()) {
                if (key.startsWith(CLEANED_HEADER_NAME_PREFIX)) {
                    hh.remove(key);
                }
            }
        }).build()).build();
        return exchange;
    }

}
