package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public abstract class AuthCertificateInterceptorBaseHttp {

    /**
     * 用户信息的头部名称
     */
    protected static final String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 认证头名称
     */
    protected static final String AUTH_HEADER = "Authorization";

    /**
     * 认证头类型
     */
    protected static final String AUTH_HEADER_TOKEN_TYPE = "Bearer";

    /**
     * 认证头必须的前缀
     */
    protected static final String AUTH_HEADER_TOKEN_PREFIX = AUTH_HEADER_TOKEN_TYPE + " ";

    /**
     * Query 方式 ACCESS TOKEN 参数 1
     */
    protected static final String ACCESS_TOKEN_PARAM1 = "accessToken";

    /**
     * Query 方式 ACCESS TOKEN 参数 2
     */
    protected static final String ACCESS_TOKEN_PARAM2 = "access_token";

    /**
     * AUTH COOKIE ACCESS TOKEN 名称
     */
    protected static String AUTH_COOKIE_ACCESS_TOKEN_NAME = "P6E_AUTH_ACCESS_TOKEN";

    /**
     * AUTH COOKIE REFRESH TOKEN 名称
     */
    protected static String AUTH_COOKIE_REFRESH_TOKEN_NAME = "P6E_AUTH_ACCESS_TOKEN";

    /**
     * 获取请求参数对应的值
     *
     * @return 结果值
     */
    protected String getParamAccessToken(ServerHttpRequest request) {
        final MultiValueMap<String, String> qp = request.getQueryParams();
        for (final String key : List.of(ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2)) {
            final List<String> values = qp.get(key);
            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }

    protected Mono<Boolean> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken) {
        final ResponseCookie accessTokenCookie = ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessToken).build();
        final ResponseCookie refreshTokenCookie = ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessToken).build();
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return Mono.just(true);
    }

    protected Mono<String> getHttpCookieAccessToken(ServerHttpRequest request) {
        final MultiValueMap<String, HttpCookie> multi = request.getCookies();
        for (final String key : multi.keySet()) {
            if (key.equalsIgnoreCase(AUTH_COOKIE_ACCESS_TOKEN_NAME)) {
                final HttpCookie cookie = multi.getFirst(key);
                if (cookie != null) {
                    return Mono.just(cookie.getValue().trim());
                }
            }
        }
        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                this.getClass(),
                "fun execute(ServerWebExchange exchange)",
                "Auth exception. [No authentication token found]"
        ));
    }

    protected Mono<String> getHttpLocalStorageAccessToken(ServerHttpRequest request) {
        String accessToken = null;
        final List<String> authorizations = request.getHeaders().get(AUTH_HEADER);
        if (authorizations != null && authorizations.size() > 0) {
            final String authorization = authorizations.get(0);
            if (StringUtils.hasText(authorization)
                    && authorization.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
                accessToken = authorization.substring(AUTH_HEADER_TOKEN_PREFIX.length());
            }
        }
        if (accessToken == null) {
            accessToken = getParamAccessToken(request);
        }
        if (accessToken == null) {
            return Mono.error(GlobalExceptionContext.exceptionAuthException(
                    this.getClass(),
                    "fun execute(ServerWebExchange exchange)",
                    "Auth exception. [No authentication token found]"
            ));
        } else {
            return Mono.just(accessToken);
        }
    }

}
