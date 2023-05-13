package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.oauth2.AuthReactiveClientRegistrationRepository;
import club.p6e.coat.gateway.auth.oauth2.AuthReactiveOAuth2AuthorizedClientService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@Configuration
@EnableWebFluxSecurity
public class AuthSecurityConfiguration {

    public AuthenticationWebFilter createAuthenticationWebFilter(
            ReactiveAuthenticationManager manager,
            ServerAuthenticationConverter converter
    ) {
        final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(converter);
        authenticationWebFilter.setAuthenticationFailureHandler(
                new ServerAuthenticationEntryPointFailureHandler(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        return authenticationWebFilter;
    }

    @Bean
    public SecurityWebFilterChain injectionSecurityWebFilterChainBean(
            ServerHttpSecurity http,
//            ServerWebExchangeMatcher matcher,
//            ReactiveAuthenticationManager manager,
//            OAuth2LoginReactiveAuthenticationManager manager2,
//            ServerAuthenticationConverter converter,
            ReactiveClientRegistrationRepository clientRegistrationRepository
    ) {
//        final AuthenticationWebFilter filter = createAuthenticationWebFilter(manager, converter);
        return http
                .oauth2Login(new Customizer<ServerHttpSecurity.OAuth2LoginSpec>() {
                    @Override
                    public void customize(ServerHttpSecurity.OAuth2LoginSpec spec) {
                        WebClientReactiveAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                                new WebClientReactiveAuthorizationCodeTokenResponseClient();
                        spec
                                .authenticationManager(new OAuth2AuthorizationCodeReactiveAuthenticationManager(accessTokenResponseClient))
//                                .authenticationManager(manager2)
                                .authenticationConverter(new ServerAuthenticationConverter() {
                                    @Override
                                    public Mono<Authentication> convert(ServerWebExchange exchange) {
                                        return null;
                                    }
                                })
                                .authenticationMatcher(new PathPatternParserServerWebExchangeMatcher("/b/**"))
                                .clientRegistrationRepository(clientRegistrationRepository);
                    }
                })
//                .securityMatcher(matcher)
//                .httpBasic().disable()
//                .formLogin().disable()
//                .csrf().disable()
//                .addFilterAfter(filter, SecurityWebFiltersOrder.AUTHORIZATION)
                .exceptionHandling(new Customizer<ServerHttpSecurity.ExceptionHandlingSpec>() {
                    @Override
                    public void customize(ServerHttpSecurity.ExceptionHandlingSpec exceptionHandlingSpec) {
                        exceptionHandlingSpec.authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/login/oauth2"));
                        exceptionHandlingSpec.accessDeniedHandler(new ServerAccessDeniedHandler() {
                            @Override
                            public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
                                denied.fillInStackTrace().printStackTrace();
                                return null;
                            }
                        });
                    }
                })
                .build();
    }



}
