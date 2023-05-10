package club.p6e.coat.gateway.auth;

import com.nimbusds.oauth2.sdk.client.ClientRegistrationRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.server.DefaultServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.*;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
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
            ServerWebExchangeMatcher matcher,
            ReactiveAuthenticationManager manager,
            ServerAuthenticationConverter converter
    ) {
        final AuthenticationWebFilter filter = createAuthenticationWebFilter(manager, converter);
        return http
                .oauth2Login(new Customizer<ServerHttpSecurity.OAuth2LoginSpec>() {
                    @Override
                    public void customize(ServerHttpSecurity.OAuth2LoginSpec spec) {
                        spec
                                .authenticationManager(new ReactiveAuthenticationManager() {
                                    @Override
                                    public Mono<Authentication> authenticate(Authentication authentication) {
                                        System.out.println(authentication);
                                        return null;
                                    }
                                })
                                .authenticationConverter(converter)
                                .authenticationMatcher(matcher)
                                .clientRegistrationRepository(new ReactiveClientRegistrationRepository() {

                                    @Override
                                    public Mono<ClientRegistration> findByRegistrationId(String registrationId) {
                                        return Mono.just(ClientRegistration
                                                .withRegistrationId(registrationId)
                                                .clientId("client_id")
                                                .clientName("client_name")
                                                .clientSecret("client_secret")
                                                .authorizationUri("http://localhost:9999/callback")
                                                .redirectUri("http://localhost:8888/callback")
                                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                                .scope("all")
                                                .build());
                                    }
                                });
                    }
                })
//                .securityMatcher(matcher)
//                .httpBasic().disable()
//                .formLogin().disable()
//                .csrf().disable()
//                .addFilterAfter(filter, SecurityWebFiltersOrder.AUTHORIZATION)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new RedirectServerAuthenticationEntryPoint("/login/oauth2"))
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationRequestResolver(this.authorizationRequestResolver())
                )
                .build();
    }


    private ServerOAuth2AuthorizationRequestResolver authorizationRequestResolver() {
        return new ServerOAuth2AuthorizationRequestResolver() {

            final ServerWebExchangeMatcher authorizationRequestMatcher =
                    new PathPatternParserServerWebExchangeMatcher("/oauth2/authorize");

            @Override
            public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange) {
                return this.authorizationRequestMatcher
                        .matches(exchange)
                        .filter(ServerWebExchangeMatcher.MatchResult::isMatch)
                        .flatMap((clientRegistrationId) -> resolve(exchange, "un"));
            }

            @Override
            public Mono<OAuth2AuthorizationRequest> resolve(ServerWebExchange exchange, String clientRegistrationId) {
//                return findByRegistrationId(exchange, clientRegistrationId)
//                        .map((clientRegistration) -> authorizationRequest(exchange, clientRegistration));
                // zidingyi
                return Mono.just(ClientRegistration
                                .withRegistrationId("registrationId")
                                .clientId("client_id")
                                .clientName("client_name")
                                .clientSecret("client_secret")
                                .authorizationUri("http://localhost:9999/callback")
                                .redirectUri("http://localhost:8888/callback")
                                .tokenUri("http://localhost:6666/callback")
                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                .scope("all")
                                .build())
                        .flatMap(r -> {
                            // 验证
                            if (1 + 1 == 2) {
                                return Mono.error(new RuntimeException("error"));
                            }

                            OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.authorizationCode();
                            builder.clientId(r.getClientId()).authorizationUri("http://localhost:9999/callback")
                                    .redirectUri(r.getRedirectUri()).scope(String.valueOf(r.getScopes())).state("123456");
                            return Mono.just(builder.build());
                        });
            }
        };
    }


}
