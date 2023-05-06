package club.p6e.coat.gateway.auth;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthServerBasicTokenAuthenticationConverter implements ServerAuthenticationConverter {

    private static final String BASIC = "basic";
    private static final String BASIC_HEADER = BASIC + " ";

    private static final String ACCESS_TOKEN_URL_QUERY1 = "accessToken";
    private static final String ACCESS_TOKEN_URL_QUERY2 = "access_token";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        String token = null;
        final ServerHttpRequest request = exchange.getRequest();
        final String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWithIgnoreCase(authorization, BASIC_HEADER)) {
            token = authorization.substring(BASIC_HEADER.length());
        }
        if (token == null) {
            final MultiValueMap<String, String> query = request.getQueryParams();
            token = query.getFirst(ACCESS_TOKEN_URL_QUERY1);
            if (token == null) {
                token = query.getFirst(ACCESS_TOKEN_URL_QUERY2);
            }
        }
        System.out.println("token " + token);
        if (token == null) {
            return Mono.empty();
        }
        return Mono.just(new TestingAuthenticationToken("*", "*","*"));
    }
}

/**
 * 在 Spring Reactive Security 中，可以使用以下拦截器进行认证和授权的定制：
 * <p>
 * AuthenticationWebFilter：用于处理认证（Authentication）的拦截器，负责从请求中提取认证信息、进行认证、创建认证对象并保存到 SecurityContext 中。
 * <p>
 * AuthorizationWebFilter：用于处理授权（Authorization）的拦截器，负责对请求进行授权判断，验证用户是否有访问资源的权限。
 * <p>
 * CorsWebFilter：用于处理跨域请求（Cross-Origin Resource Sharing，CORS）的拦截器，负责处理跨域请求的相关头信息，允许或拒绝跨域请求。
 * <p>
 * CsrfWebFilter：用于处理跨站请求伪造（Cross-Site Request Forgery，CSRF）的拦截器，负责生成和验证 CSRF token，保护应用免受 CSRF 攻击。
 * <p>
 * SecurityContextServerWebExchangeWebFilter：用于在 SecurityContext 中保存认证对象的拦截器，负责将认证对象保存到 SecurityContext 中，以便在后续请求中进行访问。
 * <p>
 * LogoutWebFilter：用于处理登出（Logout）请求的拦截器，负责处理用户登出操作，例如清除认证信息、执行登出回调等。
 * <p>
 * ServerOAuth2AuthorizationCodeAuthenticationTokenServerWebExchangeConverter：用于处理 OAuth2 授权码模式认证请求的拦截器，负责从请求中提取授权码并进行认证操作。
 * <p>
 * 以上是一些常用的 Spring Reactive Security 拦截器，可以根据实际需求和业务逻辑进行选择和配置，以实现定制化的认证和授权策略。更多的拦截器和使用方式，请参考 Spring Security 的官方文档和示例代码。
 */