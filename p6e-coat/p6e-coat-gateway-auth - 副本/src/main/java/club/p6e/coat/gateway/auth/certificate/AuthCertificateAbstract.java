package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class AuthCertificateAbstract implements AuthCertificate {

    @Override
    public Mono<Object> execute(ServerWebExchange exchange, Object o) {
        if (o instanceof final AuthUserDetails d) {
            return use(exchange, d);
        } else {
            return Mono.just(o);
        }
    }

    public abstract Mono<Object> use(ServerWebExchange exchange, AuthUserDetails authUserDetails);

}


