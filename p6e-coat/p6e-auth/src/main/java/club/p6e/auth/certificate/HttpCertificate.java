package club.p6e.auth.certificate;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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
public class HttpCertificate {

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
     * Query 方式 ACCESS TOKEN
     */
    protected static List<String> ACCESS_TOKEN_PARAM = List.of("accessToken", "access_token");

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
     * 时间格式化对象
     */
    protected static DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认的实现
     */
    private static Achieve ACHIEVE = new Achieve();

    /**
     * 重写默认的实现
     */
    public static void setAchieve(Achieve achieve) {
        ACHIEVE = achieve;
    }

    /**
     * 获取实现对象
     *
     * @return 实现对象
     */
    public static Achieve getAchieve() {
        return ACHIEVE;
    }

    /**
     * 获取用户信息的头名称
     *
     * @return 用户信息的头名称
     */
    public static String getUserHeaderName() {
        return USER_HEADER_NAME;
    }

    /**
     * 获取认证头类型
     *
     * @return 认证头类型
     */
    public static String getAuthHeaderTokenType() {
        return AUTH_HEADER_TOKEN_TYPE;
    }

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
    public static Mono<String> getHttpCookieToken(ServerHttpRequest request) {
        return ACHIEVE.getHttpCookieToken(request);
    }

    /**
     * 从请求头或者参数中获取令牌
     *
     * @param request 请求对象
     * @return 结果值
     */
    public static Mono<String> getHttpLocalStorageToken(ServerHttpRequest request) {
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
    public static String jwtDecode(String token, String secret) {
        return ACHIEVE.jwtDecode(token, secret);
    }

    public interface Specification {

        /**
         * 设置认证头名称
         *
         * @param auth 认证头名称
         */
        public void setAuthHeader(String auth);

        /**
         * 设置用户信息的头部名称
         *
         * @param name 用户信息的头部名称
         */
        public void setUserHeaderName(String name);

        /**
         * 设置认证头类型
         *
         * @param type 认证头类型
         */
        public void setAuthHeaderTokenType(String type);

        /**
         * 设置认证头必须的前缀
         *
         * @param prefix 认证头必须的前缀
         */
        public void setAuthHeaderTokenPrefix(String prefix);

        /**
         * 设置 ACCESS TOKEN 请求参数名称
         *
         * @param params 请求参数名称
         */
        public void setTokenParams(List<String> params);

        /**
         * 设置 AUTH COOKIE ACCESS TOKEN 名称
         *
         * @param name AUTH COOKIE ACCESS TOKEN 名称
         */
        public void setAuthCookieAccessTokenName(String name);

        /**
         * 设置  AUTH COOKIE REFRESH TOKEN 名称
         *
         * @param name AUTH COOKIE REFRESH TOKEN 名称
         */
        public void setAuthCookieRefreshTokenName(String name);

        /**
         * 设置过期时间
         *
         * @param time 过期时间
         */
        public void setExpirationTime(long time);

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
        public String jwtDecode(String token, String secret);
    }

    public static class Achieve implements Specification {

        @Override
        public void setAuthHeader(String auth) {
            HttpCertificate.AUTH_HEADER = auth;
        }

        @Override
        public void setUserHeaderName(String name) {
            HttpCertificate.USER_HEADER_NAME = name;
        }

        @Override
        public void setAuthHeaderTokenType(String type) {
            HttpCertificate.AUTH_HEADER_TOKEN_TYPE = type;
        }

        @Override
        public void setAuthHeaderTokenPrefix(String prefix) {
            HttpCertificate.AUTH_HEADER_TOKEN_PREFIX = prefix;
        }

        @Override
        public void setTokenParams(List<String> params) {
            HttpCertificate.ACCESS_TOKEN_PARAM = params;
        }

        @Override
        public void setAuthCookieAccessTokenName(String name) {
            HttpCertificate.AUTH_COOKIE_ACCESS_TOKEN_NAME = name;
        }

        @Override
        public void setAuthCookieRefreshTokenName(String name) {
            HttpCertificate.AUTH_COOKIE_REFRESH_TOKEN_NAME = name;
        }

        @Override
        public void setExpirationTime(long time) {
            HttpCertificate.EXPIRATION_TIME = time;
        }

        @Override
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

        @Override
        public String getQueryParamToken(ServerHttpRequest request) {
            final MultiValueMap<String, String> params = request.getQueryParams();
            for (final String key : ACCESS_TOKEN_PARAM) {
                final List<String> values = params.get(key);
                if (values != null && values.size() > 0) {
                    return values.get(0);
                }
            }
            return null;
        }

        @Override
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

        @Override
        public Mono<String> getHttpLocalStorageToken(ServerHttpRequest request) {
            String token = getHeaderToken(request);
            if (token == null) {
                token = getQueryParamToken(request);
            }
            return token == null ? Mono.empty() : Mono.just(token);
        }

        @Override
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

        @Override
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

        @Override
        public String jwtCreate(String uid, String content, String secret) {
            final Date date = Date.from(LocalDateTime.now()
                    .plusSeconds(EXPIRATION_TIME)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
            );
            return JWT
                    .create()
                    .withAudience(uid)
                    .withExpiresAt(date)
                    .withSubject(content)
                    .sign(Algorithm.HMAC256(secret));
        }

        @Override
        public String jwtDecode(String token, String secret) {
            try {
                return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject();
            } catch (Exception e) {
                return null;
            }
        }
    }

}
