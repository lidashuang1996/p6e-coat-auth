package club.p6e.coat.gateway.auth.oauth2;

import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class A implements ServerAuthorizationRequestRepository {
    @Override
    public Mono loadAuthorizationRequest(ServerWebExchange exchange) {
        System.out.println("a >>>> loadAuthorizationRequest");
        System.out.println(exchange);
        return null;
    }

    @Override
    public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
        System.out.println("a >>>> saveAuthorizationRequest");
        System.out.println(exchange);
        return null;
    }

    @Override
    public Mono removeAuthorizationRequest(ServerWebExchange exchange) {
        System.out.println("a >>>> removeAuthorizationRequest");
        System.out.println(exchange);
        return null;
    }
}
