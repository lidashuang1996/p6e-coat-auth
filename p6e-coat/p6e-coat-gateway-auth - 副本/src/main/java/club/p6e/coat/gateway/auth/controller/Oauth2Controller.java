package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.error.ParameterException;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.gateway.auth.service.Oauth2TokenService;
import club.p6e.coat.gateway.auth.utils.TemplateParser;
import jakarta.annotation.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.endpoint.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {

    /**
     * 资源类型
     */
    private MediaType type;

    /**
     * 资源内容
     */
    private String content;

    @Resource
    private Oauth2TokenService service;
    private final Oauth2ClientRepository repository;
    private final ReactiveClientRegistrationRepository clientRegistrationRepository;

    public Oauth2Controller(Oauth2ClientRepository repository, ReactiveClientRegistrationRepository clientRegistrationRepository) {
        this.repository = repository;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * IP 头部名称
     */
    @SuppressWarnings("ALL")
    private static final String IP_HEADER_NAME = "P6e-IP";

    /**
     * http://127.0.0.1:8080/oauth2/authorize?scope=user_info&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&responseType=code&state=111&clientSecret=123456
     *
     * @param exchange
     * @return
     */
    @RequestMapping("/authorize")
    public Mono<Object> authorize(ServerWebExchange exchange) {
        final String state = getState(exchange);
        final String scope = getScope(exchange);
        final String clientId = getClientId(exchange);
        final String redirectUri = getRedirectUri(exchange);
        final String responseType = getResponseType(exchange);
        final String cc = content == null ? "@{voucher}" : content;
        final MediaType t = type == null ? MediaType.TEXT_PLAIN : type;
        final ServerHttpRequest request = exchange.getRequest();
        final ServerHttpResponse response = exchange.getResponse();
        final HttpHeaders headers = request.getHeaders();
        final List<String> ips = headers.get(IP_HEADER_NAME);
        String ip;
        if (ips != null && ips.size() > 0) {
            ip = ips.get(0);
        } else {
            ip = "0.0.0.0";
        }
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
                    final Map<String, String> map = new HashMap<>(2);
                    map.put(AuthVoucherContext.IP, ip);
                    map.put(AuthVoucherContext.OAUTH2, "true");
                    map.put(AuthVoucherContext.OAUTH2_DATE, String.valueOf(System.currentTimeMillis()));
                    return AuthVoucherContext
                            .create(map)
                            .map(v -> TemplateParser.execute(cc, "voucher", v.getMark()))
                            .flatMap(r -> response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(r.getBytes()))));
                });
    }


    /**
     * 写入资源类型
     *
     * @param type 资源类型
     */
    public void setType(MediaType type) {
        this.type = type;
    }

    /**
     * 写入资源内容
     *
     * @param content 资源内容
     */
    public void setContent(String content) {
        this.content = content;
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
    ReactiveOAuth2AuthorizedClientManager manager;

    /**
     * http://127.0.0.1:8080/oauth2/token?scope=user_info&clientId=123456&redirectUri=http://127.0.0.1:9999&grantType=code&state=111&clientSecret=123456&code=code
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
        return switch (grantType) {
            case "password": {
                return service.grantTypePassword();
            }
            case "client_credentials": {
                return service.grantTypeClientCredentials();
            }
            case "refresh_token": {
                return service.grantTypeRefreshToken();
            }
            case "authorization_code": {
                return service.grantTypeAuthorizationCode();
            }
            default:
                yield Mono.error(new RuntimeException());
        };
    }

}
