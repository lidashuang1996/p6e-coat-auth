package club.p6e.coat.gateway.auth.oauth2;

import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthenticationMethod;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * http://127.0.0.1:8080/oauth2/authorization/123456
 * http://127.0.0.1:8080/oauth2/authorize?scope=user_info&clientId=123456&redirectUri=http://127.0.0.1:9999&responseType=code
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthReactiveClientRegistrationRepository implements ReactiveClientRegistrationRepository {

    private final Oauth2ClientRepository repository;

    public AuthReactiveClientRegistrationRepository(Oauth2ClientRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<ClientRegistration> findByRegistrationId(String registrationId) {
        return repository
                .findOneByClientId(registrationId)
                .map(m -> {
                    System.out.println("MMMM " + m);
                    return ClientRegistration
                            .withRegistrationId(m.getClientId())
                            .clientId(m.getClientId())
                            .clientName(m.getClientName())
                            .clientSecret(m.getClientSecret())
                            .scope(m.getScope())
                            .redirectUri(m.getRedirectUri())
                            .tokenUri("/oauth/token2222")
                            .authorizationUri("/oauth2/authorize")
                            .userInfoUri("{baseUrl}/user/info")
                            .userInfoAuthenticationMethod(AuthenticationMethod.HEADER)
                            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_POST)
                            .build();
                });
    }

}
