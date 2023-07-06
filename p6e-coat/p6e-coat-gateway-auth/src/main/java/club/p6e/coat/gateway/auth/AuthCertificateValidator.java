package club.p6e.coat.gateway.auth;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificateValidator {
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange);

    public Mono<String> accessToken(String token);
    public Mono<String> refreshToken(String token);

}
