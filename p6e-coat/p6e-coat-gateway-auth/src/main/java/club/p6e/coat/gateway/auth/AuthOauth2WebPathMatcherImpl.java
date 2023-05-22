package club.p6e.coat.gateway.auth;

import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthOauth2WebPathMatcherImpl implements AuthOauth2WebPathMatcher {

    private final PathPatternParserServerWebExchangeMatcher
            matcher = new PathPatternParserServerWebExchangeMatcher("/oauth2/api/**");

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        return matcher.matches(exchange);
    }

}
