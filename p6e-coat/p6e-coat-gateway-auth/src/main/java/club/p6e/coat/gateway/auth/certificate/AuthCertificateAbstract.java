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
    public Mono<Void> execute(ServerWebExchange exchange, Object o) {
        if (o instanceof final AuthUserDetails d) {
            return write(exchange, use(exchange, d));
        } else {
            return write(exchange, o);
        }
    }

    private Mono<Void> write(ServerWebExchange exchange, Object o) {
        if (o == null) {
            return Mono.empty();
        }
        String result = JsonUtil.toJson(o);
        result = result == null ? "" : result;
        final ServerHttpResponse response = exchange.getResponse();
        return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(result.getBytes())));
    }

    public abstract Object use(ServerWebExchange exchange, AuthUserDetails authUserDetails);

}


