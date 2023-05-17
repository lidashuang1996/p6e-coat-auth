package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthServerAuthenticationConverter;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthServerHttpCookieCacheAuthenticationConverter
        extends AuthHttpCookieCertificate implements AuthServerAuthenticationConverter {

    private static final String USER_HEADER_NAME = "P6e-User-Info";

    private final AuthCache cache;

    public AuthServerHttpCookieCacheAuthenticationConverter(AuthCache cache) {
        this.cache = cache;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final List<HttpCookie> httpCookies = getAccessTokenCookie(request);
        if (httpCookies == null || httpCookies.size() == 0) {
            return Mono.error(new RuntimeException());
        } else {
            final HttpCookie httpCookie = httpCookies.get(0);
            final String accessToken = httpCookie.getValue().trim();
            return Mono
                    .just(accessToken)
                    .flatMap(cache::getAccessToken)
                    .flatMap(t -> cache
                            .getUser(t.getUid())
                            .map(u -> {
                                exchange.mutate().request(request.mutate().header(USER_HEADER_NAME, u).build()).build();
                                return new TestingAuthenticationToken(t.getUid(), null, "");
                            })
                    );
        }
    }

}
