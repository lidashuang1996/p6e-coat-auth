package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.error.ParameterException;
import club.p6e.coat.gateway.auth.oauth2.AuthReactiveClientRegistrationRepository;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import jakarta.annotation.Resource;
import org.springframework.boot.web.embedded.netty.NettyWebServer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthorizationCodeReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginReactiveAuthenticationManager;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.ReactiveOAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveJwtBearerTokenResponseClient;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.endpoint.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

    private final Oauth2ClientRepository repository;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public Oauth2Controller(Oauth2ClientRepository repository, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.repository = repository;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @RequestMapping("/authorize")
    public Mono<Object> authorize(ServerWebExchange exchange) {
        final String state = getState(exchange);
        final String scope = getScope(exchange);
        final String clientId = getClientId(exchange);
        final String redirectUri = getRedirectUri(exchange);
        final String responseType = getResponseType(exchange);
        return repository
                .findOneByClientId(clientId)
                .switchIfEmpty(Mono.error(new ParameterException(this.getClass(), "", "")))
                .flatMap(c -> {
                    if (state == null
                            || scope == null
                            || clientId == null
                            || redirectUri == null
                            || responseType == null) {
                        return Mono.error(new ParameterException(this.getClass(), "", ""));
                    }
                    if (!Objects.equals(clientId, c.getClientId())) {
                        return Mono.error(new ParameterException(this.getClass(), "", ""));
                    }
                    if (!testScope(scope, partitionToList(c.getScope()))) {
                        return Mono.error(new ParameterException(this.getClass(), "", ""));
                    }
                    if (!testRedirectUri(redirectUri, partitionToList(c.getRedirectUri()))) {
                        return Mono.error(new ParameterException(this.getClass(), "", ""));
                    }
                    if (!testType(responseType, partitionToList(String.valueOf(c.getType())))
                            || !OAuth2AuthorizationResponseType.CODE.getValue().equals(responseType)) {
                        return Mono.error(new ParameterException(this.getClass(), "", ""));
                    }
                    return Mono.just("SUCCESS !!!! Login .. ");
                });
    }


    private String getQueryParam(ServerWebExchange exchange, String... params) {
        final ServerHttpRequest request = exchange.getRequest();
        final MultiValueMap<String, String> qp = request.getQueryParams();
        for (final String item : params) {
            final String content = qp.getFirst(item);
            if (content != null) {
                return content;
            }
        }
        return null;
    }


    private String getState(ServerWebExchange exchange) {
        return getQueryParam(exchange, "state");
    }

    private String getScope(ServerWebExchange exchange) {
        return getQueryParam(exchange, "scope");
    }

    private String getClientId(ServerWebExchange exchange) {
        return getQueryParam(exchange, "client_id", "clientId");
    }

    private String getRedirectUri(ServerWebExchange exchange) {
        return getQueryParam(exchange, "redirect_uri", "redirectUri");
    }

    private String getResponseType(ServerWebExchange exchange) {
        return getQueryParam(exchange, "response_type", "responseType");
    }


    private String getGrantType(ServerWebExchange exchange) {
        return getQueryParam(exchange, "grant_type", "grantType");
    }

    private String getCode(ServerWebExchange exchange) {
        return getQueryParam(exchange, "code");
    }

    private String getClientSecret(ServerWebExchange exchange) {
        return getQueryParam(exchange, "client_secret", "clientSecret");
    }

    private List<String> partitionToList(String content) {
        if (content == null || content.isEmpty()) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(content.split(","));
        }
    }

    private boolean testType(String type, List<String> types) {
        for (final String i : types) {
            if (type.equalsIgnoreCase(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean testScope(String scope, List<String> scopes) {
        for (final String i : scopes) {
            if (scope.equalsIgnoreCase(i)) {
                return true;
            }
        }
        return false;
    }

    private boolean testRedirectUri(String uri, List<String> uris) {
        for (final String i : uris) {
            if (uri.equalsIgnoreCase(i)) {
                return true;
            }
        }
        return false;
    }

    @Resource
    AuthReactiveClientRegistrationRepository authReactiveClientRegistrationRepository;
//
//    @Resource
//    ServerOAuth2AuthorizedClientRepository auth2AuthorizedClientService;

    @Resource
    ReactiveOAuth2AuthorizedClientManager manager;

    /**
     http://127.0.0.1:8080/oauth2/token?scope=user_info&clientId=123456&redirectUri=http://127.0.0.1:9999&grantType=code&state=111&clientSecret=123456&code=code
     *
     * @param exchange
     * @return
     */
    @RequestMapping("/token")
    public Mono<Object> token(ServerWebExchange exchange) {
        final String code = getCode(exchange);
        final String grantType = getGrantType(exchange);
        final String redirectUri = getRedirectUri(exchange);
        final String clientId = getClientId(exchange);
        final String clientSecret = getClientSecret(exchange);
        if (code == null
                || grantType == null
                || redirectUri == null
                || clientId == null
                || clientSecret == null) {
            return Mono.error(new ParameterException(this.getClass(), "", ""));
        }
        return authReactiveClientRegistrationRepository
                .findByRegistrationId(clientId)
                .flatMap(r -> {
                    return manager.authorize(OAuth2AuthorizeRequest
                            .withClientRegistrationId(clientId)
                            .principal("principal")
                            .build());
                }).map(f -> {
                    System.out.println(f);
                    System.out.println(
                            f.getAccessToken()
                    );
                    System.out.println(
                            f.getRefreshToken()
                    );
                    System.out.println(
                            f.getPrincipalName()
                    );
                    return f;
                });


//        OAuth2AuthorizationRequest authorizationRequest = OAuth2AuthorizationRequest.authorizationCode()
//                .clientId(clientRegistration.getClientId())
//                .redirectUri(clientRegistration.getRedirectUri())
//                .state("state")
//                .build();
////        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
////                .withClientRegistrationId(clientId)
////                .principal("principal")
////                .build();
//        authorizationRequest.getAuthorizationRequestUri();
//        return aaa()
//                .authorize(request)
//                .map(JsonUtil::toJson);


//        // 使用授权码获取访问令牌并请求用户信息
//        @GetMapping("/userInfo")
//        public Mono<String> userInfo(@RequestParam("code") String code) {
//            ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("your-client-registration-id");
//            ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//            OAuth2AuthorizationCodeGrantRequest authorizationCodeGrantRequest = new OAuth2AuthorizationCodeGrantRequest(
//                    clientRegistration, new AuthorizationCodeAuthenticationToken(clientRegistration, new AuthorizationCodeAuthenticationToken(code)));
//            return authorizedClientManager.authorize(authorizationCodeGrantRequest)
//                    .flatMap(authorizedClient -> WebClient.create().get()
//                            .uri("https://your-user-info-uri")
//                            .attributes(oauth2AuthorizedClient(authorizedClient))
//                            .retrieve()
//                            .bodyToMono(String.class));
//        }
    }

}
