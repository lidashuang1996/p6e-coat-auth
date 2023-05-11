package club.p6e.coat.gateway.auth.oauth2;

import club.p6e.coat.gateway.auth.cache.Oauth2AuthCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ServerOAuth2AuthorizedClientRepository.class,
        ignored = AuthReactiveOAuth2AuthorizedClientService.class
)
public class AuthReactiveOAuth2AuthorizedClientService implements ServerOAuth2AuthorizedClientRepository {

    private final AuthReactiveClientRegistrationRepository repository;

    public AuthReactiveOAuth2AuthorizedClientService(AuthReactiveClientRegistrationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<A> loadAuthorizedClient(String clientRegistrationId, Authentication principal, ServerWebExchange exchange) {
        System.out.println("loadAuthorizedClient");
        System.out.println(
                clientRegistrationId + "  " +
                        principal
        );
        return repository
                .findByRegistrationId(clientRegistrationId)
                .map(c -> new A(c, principal.getName(),
                        new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, "9999", Instant.now(), Instant.now().plusSeconds(3600)),
                        new OAuth2RefreshToken("8888", null)
                ));
    }

    @Override
    public Mono<Void> saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal, ServerWebExchange exchange) {
        System.out.println("saveAuthorizedClient");
        return null;
    }

    @Override
    public Mono<Void> removeAuthorizedClient(String clientRegistrationId, Authentication principal, ServerWebExchange exchange) {
        System.out.println("removeAuthorizedClient");
        return null;
    }

    private static class A extends OAuth2AuthorizedClient {
        public A(ClientRegistration clientRegistration, String principalName, OAuth2AccessToken accessToken, OAuth2RefreshToken refreshToken) {
            super(clientRegistration, principalName, accessToken, refreshToken);
        }
    }

}
