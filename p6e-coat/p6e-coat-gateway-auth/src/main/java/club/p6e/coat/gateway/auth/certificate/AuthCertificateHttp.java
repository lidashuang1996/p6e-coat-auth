package club.p6e.coat.gateway.auth.certificate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
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
    protected static String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 认证头名称
     */
    protected static String AUTH_HEADER = "Authorization";

    /**
     * 认证头类型
     */
    protected static String AUTH_HEADER_TOKEN_TYPE = "Bearer";

    /**
     * 认证头必须的前缀
     */
    protected static String AUTH_HEADER_TOKEN_PREFIX = AUTH_HEADER_TOKEN_TYPE + " ";

    /**
     * Query 方式 ACCESS TOKEN 参数 1
     */
    protected static String ACCESS_TOKEN_PARAM1 = "accessToken";

    /**
     * Query 方式 ACCESS TOKEN 参数 2
     */
    protected static String ACCESS_TOKEN_PARAM2 = "access_token";

    /**
     * AUTH COOKIE ACCESS TOKEN 名称
     */
    protected static String AUTH_COOKIE_ACCESS_TOKEN_NAME = "P6E_AUTH_ACCESS_TOKEN";

    /**
     * AUTH COOKIE REFRESH TOKEN 名称
     */
    protected static String AUTH_COOKIE_REFRESH_TOKEN_NAME = "P6E_AUTH_REFRESH_TOKEN";

    /**
     * 认证过期时间
     */
    protected static long EXPIRATION_TIME = 3600;

    /**
     * JWT 内容
     */
    protected static String CONTENT = "content";

    /**
     * 时间格式化对象
     */
    protected static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认的实现
     */
    private static final Achieve ACHIEVE = new Achieve();

    /**
     * 从请求头中获取令牌
     *
     * @param request 请求对象
     * @return 结果值
     */
    public static String getHeaderToken(ServerHttpRequest request) {
        return ACHIEVE.getHeaderToken(request);
    }

    /**
     * 从请求参数中获取令牌
     *
     * @param request 请求对象
     * @return 结果值
     */
    public static String getQueryParamToken(ServerHttpRequest request) {
        return ACHIEVE.getQueryParamToken(request);
    }

    /**
     * 从请求 cookies 中获取令牌
     *
     * @param request 请求对象
     * @return 结果值
     */
    public Mono<String> getHttpCookieToken(ServerHttpRequest request) {
        return ACHIEVE.getHttpCookieToken(request);
    }

    /**
     * 从请求头或者参数中获取令牌
     *
     * @param request 请求对象
     * @return 结果值
     */
    public Mono<String> getHttpLocalStorageToken(ServerHttpRequest request) {
        return ACHIEVE.getHttpLocalStorageToken(request);
    }

    /**
     * 写入 cookie 令牌
     *
     * @param response     返回对象
     * @param accessToken  认证令牌
     * @param refreshToken 刷新令牌
     * @return 结果值
     */
    public static Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken) {
        return setHttpCookieToken(response, accessToken, refreshToken, LocalDateTime.now().format(DTF));
    }

    /**
     * 写入 cookie 令牌
     *
     * @param response     返回对象
     * @param accessToken  认证令牌
     * @param refreshToken 刷新令牌
     * @param result       结果对象
     * @return 结果值
     */
    public static Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken, Object result) {
        return ACHIEVE.setHttpCookieToken(response, accessToken, refreshToken, result);
    }

    /**
     * 写入本地缓存令牌
     *
     * @param accessToken  认证令牌
     * @param refreshToken 刷新令牌
     * @return 结果值
     */
    public static Mono<Map<String, Object>> setHttpLocalStorageToken(String accessToken, String refreshToken) {
        return setHttpLocalStorageToken(accessToken, refreshToken, null);
    }

    /**
     * 写入本地缓存令牌
     *
     * @param accessToken  认证令牌
     * @param refreshToken 刷新令牌
     * @param data         数据对象
     * @return 结果值
     */
    public static Mono<Map<String, Object>> setHttpLocalStorageToken(String accessToken, String refreshToken, Map<String, Object> data) {
        return ACHIEVE.setHttpLocalStorageToken(accessToken, refreshToken, data);
    }

    /**
     * JWT 加密
     *
     * @param uid     用户编号
     * @param content 用户信息
     * @param secret  密钥
     * @return 令牌
     */
    public static String jwtCreate(String uid, String content, String secret) {
        return ACHIEVE.jwtCreate(uid, content, secret);
    }

    /**
     * JWT 解密
     *
     * @param token  令牌
     * @param secret 密钥
     * @return 解密内容
     */
    public static String jwtRequire(String token, String secret) {
        return ACHIEVE.jwtRequire(token, secret);
    }

    public interface Specification {

        /**
         * 从请求头中获取令牌
         *
         * @param request 请求对象
         * @return 结果值
         */
        public String getHeaderToken(ServerHttpRequest request);

        /**
         * 从请求参数中获取令牌
         *
         * @param request 请求对象
         * @return 结果值
         */
        public String getQueryParamToken(ServerHttpRequest request);

        /**
         * 从请求 cookies 中获取令牌
         *
         * @param request 请求对象
         * @return 结果值
         */
        public Mono<String> getHttpCookieToken(ServerHttpRequest request);

        /**
         * 从请求头或者参数中获取令牌
         *
         * @param request 请求对象
         * @return 结果值
         */
        public Mono<String> getHttpLocalStorageToken(ServerHttpRequest request);

        /**
         * 写入 cookie 令牌
         *
         * @param response     返回对象
         * @param accessToken  认证令牌
         * @param refreshToken 刷新令牌
         * @param result       结果对象
         * @return 结果值
         */
        public Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken, Object result);

        /**
         * 写入本地缓存令牌
         *
         * @param accessToken  认证令牌
         * @param refreshToken 刷新令牌
         * @param data         数据对象
         * @return 结果值
         */
        public Mono<Map<String, Object>> setHttpLocalStorageToken(String accessToken, String refreshToken, Map<String, Object> data);

        /**
         * JWT 加密
         *
         * @param uid     用户编号
         * @param content 用户信息
         * @param secret  密钥
         * @return 令牌
         */
        public String jwtCreate(String uid, String content, String secret);

        /**
         * JWT 解密
         *
         * @param token  令牌
         * @param secret 密钥
         * @return 解密内容
         */
        public String jwtRequire(String token, String secret);
    }

    public static class Achieve implements Specification {

        public String getHeaderToken(ServerHttpRequest request) {
            final List<String> authorizations = request.getHeaders().get(AUTH_HEADER);
            if (authorizations != null && authorizations.size() > 0) {
                final String authorization = authorizations.get(0);
                if (StringUtils.hasText(authorization)
                        && authorization.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
                    return authorization.substring(AUTH_HEADER_TOKEN_PREFIX.length());
                }
            }
            return null;
        }

        public String getQueryParamToken(ServerHttpRequest request) {
            final MultiValueMap<String, String> querys = request.getQueryParams();
            for (final String key : List.of(ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2)) {
                final List<String> values = querys.get(key);
                if (values != null && values.size() > 0) {
                    return values.get(0);
                }
            }
            return null;
        }

        public Mono<String> getHttpCookieToken(ServerHttpRequest request) {
            final MultiValueMap<String, HttpCookie> cookies = request.getCookies();
            for (final String key : cookies.keySet()) {
                if (key.equalsIgnoreCase(AUTH_COOKIE_ACCESS_TOKEN_NAME)) {
                    final List<HttpCookie> values = cookies.get(key);
                    if (values != null && values.size() > 0) {
                        return Mono.just(values.get(0).getValue().trim());
                    }
                }
            }
            return Mono.empty();
        }

        public Mono<String> getHttpLocalStorageToken(ServerHttpRequest request) {
            String token = getHeaderToken(request);
            if (token == null) {
                token = getQueryParamToken(request);
            }
            return token == null ? Mono.empty() : Mono.just(token);
        }

        public Mono<Object> setHttpCookieToken(ServerHttpResponse response, String accessToken, String refreshToken, Object result) {
            final ResponseCookie accessTokenCookie = ResponseCookie
                    .from(AUTH_COOKIE_ACCESS_TOKEN_NAME, accessToken)
                    .build();
            final ResponseCookie refreshTokenCookie = ResponseCookie
                    .from(AUTH_COOKIE_REFRESH_TOKEN_NAME, refreshToken)
                    .build();
            response.addCookie(accessTokenCookie);
            response.addCookie(refreshTokenCookie);
            return Mono.just(result);
        }

        public Mono<Map<String, Object>> setHttpLocalStorageToken(
                String accessToken, String refreshToken, Map<String, Object> data) {
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

        public String jwtCreate(String uid, String content, String secret) {
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

        public String jwtRequire(String token, String secret) {
            final DecodedJWT jwt = JWT
                    .require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token);
            return jwt.getClaim(CONTENT).asString();
        }
    }

    public static class Builder {

        /**
         * 用户信息的头部名称
         */
        public static void setUserHeaderName(String name) {
            AuthCertificateHttp.USER_HEADER_NAME = name;
        }

        /**
         * 认证头名称
         */
        public static void  setAuthHeader(String header) {
            AuthCertificateHttp.AUTH_HEADER = header;
        }

        /**
         * 认证头类型
         */
        public static void setAuthHeaderTokenType(String headerTokenType) {
            AuthCertificateHttp.AUTH_HEADER_TOKEN_TYPE = headerTokenType;
        }

        /**
         * 认证头必须的前缀
         */
        public static void setAuthHeaderTokenPrefix(String headerTokenPrefix) {
            AuthCertificateHttp.AUTH_HEADER_TOKEN_PREFIX = headerTokenPrefix;
        }

        /**
         * Query 方式 ACCESS TOKEN 参数 1
         */
        public static void setACCESS_TOKEN_PARAM1(String accessTokenParam) {
            AuthCertificateHttp.ACCESS_TOKEN_PARAM1 = accessTokenParam;
        }

        /**
         * Query 方式 ACCESS TOKEN 参数 2
         */
        public static void setACCESS_TOKEN_PARAM2(String accessTokenParam) {
            AuthCertificateHttp.ACCESS_TOKEN_PARAM2 = accessTokenParam;
        }

        /**
         * AUTH COOKIE ACCESS TOKEN 名称
         */
        public static void setAUTH_COOKIE_ACCESS_TOKEN_NAME(String accessTokenName) {
            AuthCertificateHttp.AUTH_COOKIE_ACCESS_TOKEN_NAME = accessTokenName;
        }

        /**
         * AUTH COOKIE REFRESH TOKEN 名称
         */
        public static void setAUTH_COOKIE_REFRESH_TOKEN_NAME(String refreshTokenName) {
            AuthCertificateHttp.AUTH_COOKIE_REFRESH_TOKEN_NAME = refreshTokenName;
        }

        /**
         * 认证过期时间
         */
        public static void setEXPIRATION_TIME() {

        }

        /**
         * JWT 内容
         */
        public static void setContent() {

        }

        /**
         * 时间格式化对象
         */
        public static void setDateTimeFormatter() {

        }

    }
}
