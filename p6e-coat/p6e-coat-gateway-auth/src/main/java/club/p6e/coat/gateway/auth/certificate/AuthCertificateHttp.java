package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateHttp {

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
    protected static String AUTH_COOKIE_REFRESH_TOKEN_NAME = "P6E_AUTH_REFRESH_TOKEN";

    /**
     * 获取请求参数对应的值
     *
     * @return 结果值
     */
    protected String getAccessTokenParam(ServerHttpRequest request) {
        final MultiValueMap<String, String> qp = request.getQueryParams();
        for (final String key : List.of(ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2)) {
            final List<String> values = qp.get(key);
            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }

    // 创建日期时间格式化器
    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    protected Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken) {
        return setHttpCookieToken(response, accessToken, refreshToken, LocalDateTime.now().format(DTF));
    }

    protected Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken, Object result) {
        final ResponseCookie accessTokenCookie = ResponseCookie.from(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessToken).build();
        final ResponseCookie refreshTokenCookie = ResponseCookie.from(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshToken).build();
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return Mono.just(result);
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
            accessToken = getAccessTokenParam(request);
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


    /**
     * 认证过期时间
     */
    protected static final long EXPIRATION_TIME = 3600;


    protected Mono<Map<String, Object>> setHttpLocalStorageToken(String accessToken, String refreshToken) {
        return setHttpLocalStorageToken(accessToken, refreshToken, null);
    }

    protected Mono<Map<String, Object>> setHttpLocalStorageToken(String accessToken, String refreshToken, Map<String, Object> data) {
        final Map<String, Object> result = new HashMap<>(5);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("expiration", EXPIRATION_TIME);
        result.put("type", AUTH_HEADER_TOKEN_TYPE);
        if (data != null && data.size() > 0) {
            for (final String key : data.keySet()) {
                result.put(key, data.get(key));
            }
        }
        return Mono.just(result);
    }


    /**
     * JWT 内容
     */
    private static final String CONTENT = "content";


    protected String jwtCreate(String uid, String content, String secret) {
        final Date date = Date.from(LocalDateTime.now()
                .plusSeconds(EXPIRATION_TIME)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        );
        return JWT
                .create()
                .withExpiresAt(date)
                .withAudience(uid)
                .withClaim(CONTENT, content)
                .sign(Algorithm.HMAC256(secret));
    }

    protected String jwtRequire(String token, String secret) {
        final DecodedJWT jwt = JWT
                .require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
        return jwt.getClaim(CONTENT).asString();
    }
}
