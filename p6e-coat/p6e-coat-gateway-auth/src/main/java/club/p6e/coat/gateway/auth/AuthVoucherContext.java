package club.p6e.coat.gateway.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthVoucherContext implements Serializable {

    public static Mono<AuthVoucherContext> create(ServerWebExchange exchange) {
        return Mono.just(new AuthVoucherContext());
    }

}
