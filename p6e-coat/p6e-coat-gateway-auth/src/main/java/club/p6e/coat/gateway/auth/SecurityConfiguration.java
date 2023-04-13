package club.p6e.coat.gateway.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain injectingSecurityWebFilterChainBean(ServerHttpSecurity http) {
        return http.authorizeExchange().matchers(new ServerWebExchangeMatcher() {
            @Override
            public Mono<MatchResult> matches(ServerWebExchange exchange) {
                System.out.println(
                        exchange.getRequest().getPath().value()
                );
                return "/test".equals(exchange.getRequest().getPath().value()) ? MatchResult.notMatch() : MatchResult.match();
            }
        }).permitAll().and().build();
    }

}
