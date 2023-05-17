package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthServerAuthenticationConverter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthServerCacheBearerTokenAuthenticationConverter implements AuthServerAuthenticationConverter {

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return null;
    }

}
