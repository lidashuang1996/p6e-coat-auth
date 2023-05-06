package club.p6e.coat.gateway.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
@EnableWebFluxSecurity
public class AuthSecurityConfiguration {

    @Bean
    public SecurityWebFilterChain injectionSecurityWebFilterChainBean(ServerHttpSecurity http, AuthenticationWebFilter filter) {
        return http.authorizeExchange().matchers(new ServerWebExchangeMatcher() {
                    @Override
                    public Mono<MatchResult> matches(ServerWebExchange exchange) {
                        System.out.println(
                                exchange.getRequest().getPath().value()
                        );
                        return (exchange.getRequest().getPath().value()).startsWith("/test") ? MatchResult.notMatch() : MatchResult.match();
                    }
                }).permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }


    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager manager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(new AuthServerBasicTokenAuthenticationConverter());
        authenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        return authenticationWebFilter;
    }

    @Bean
    public ReactiveUserDetailsService injectionReactiveUserDetailsServiceBean() {
        return new AuthReactiveUserDetailsServiceImpl();
    }

    @Bean
    public ReactiveAuthenticationManager injectionReactiveAuthenticationManagerBean(
            PasswordEncoder passwordEncoder, ReactiveUserDetailsService reactiveUserDetailsService) {
        return new AuthReactiveAuthenticationManager(passwordEncoder, reactiveUserDetailsService);
    }

}
