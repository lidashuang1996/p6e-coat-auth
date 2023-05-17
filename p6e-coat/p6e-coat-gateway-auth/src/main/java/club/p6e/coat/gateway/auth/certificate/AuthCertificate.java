package club.p6e.coat.gateway.auth.certificate;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificate {

    public Mono<Void> execute(ServerWebExchange exchange, Object o);

}


