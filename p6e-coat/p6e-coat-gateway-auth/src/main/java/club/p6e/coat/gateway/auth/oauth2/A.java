package club.p6e.coat.gateway.auth.oauth2;

import org.springframework.security.oauth2.client.web.server.ServerAuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class A implements ServerAuthorizationRequestRepository {
    @Override
    public Mono loadAuthorizationRequest(ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono<Void> saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, ServerWebExchange exchange) {
        return null;
    }

    @Override
    public Mono removeAuthorizationRequest(ServerWebExchange exchange) {
        return null;
    }
}
