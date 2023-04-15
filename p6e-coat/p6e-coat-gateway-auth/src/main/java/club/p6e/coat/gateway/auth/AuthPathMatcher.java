package club.p6e.coat.gateway.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthPathMatcher implements ServerWebExchangeMatcher {

    private List<String> interceptUrlList = new ArrayList<>();

    @Override
    public Mono<MatchResult> matches(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();
        final String path = request.getPath().value();
        return (exchange.getRequest().getPath().value()).startsWith("/test") ? MatchResult.notMatch() : MatchResult.match();
    }

}
