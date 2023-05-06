package club.p6e.coat.gateway.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ServerAuthenticationConverter.class,
        ignored = AuthBasicTokenServerAuthenticationConverter.class
)
public class AuthBasicTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    private final AuthForeignMinistry authForeignMinistry;

    public AuthBasicTokenServerAuthenticationConverter(AuthForeignMinistry authForeignMinistry) {
        this.authForeignMinistry = authForeignMinistry;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        return Mono.just(authForeignMinistry.verificationAccessToken(request).getAuthentication());
    }

}