package club.p6e.coat.gateway.auth.foreign;

import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 外交部抽象类
 * 通过 HTTP -> HEADER 的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class ReactiveHttpHeaderAuthForeignMinistry {

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
     * Query 方式 REFRESH TOKEN 参数 1
     */
    protected static final String REFRESH_TOKEN_PARAM1 = "refreshToken";

    /**
     * Query 方式 REFRESH TOKEN 参数 2
     */
    protected static final String REFRESH_TOKEN_PARAM2 = "refresh_token";

    /**
     * 令牌过期时间
     * 令牌过期时间默认设置为 3600 秒
     */
    protected static int EXPIRATION_TIME = 3600;

    /**
     * 设置令牌过期时间
     *
     * @param expiration 设置的令牌过期时间
     */
    public static void setExpirationTime(int expiration) {
        EXPIRATION_TIME = expiration;
    }

    /**
     * 获取 ACCESS TOKEN 内容
     *
     * @return ACCESS TOKEN 内容
     */
    protected String getAccessToken(ServerRequest request) {
        String accessToken = getHeaderToken(request);
        if (accessToken == null) {
            accessToken = getParamValue(request, ACCESS_TOKEN_PARAM1, ACCESS_TOKEN_PARAM2);
        }
        return accessToken;
    }

    /**
     * 获取 REFRESH TOKEN 内容
     *
     * @return REFRESH TOKEN 内容
     */
    protected String getRefreshToken(ServerRequest request) {
        return getParamValue(request, REFRESH_TOKEN_PARAM1, REFRESH_TOKEN_PARAM2);
    }

    /**
     * 通过多个参数名称去获取请求路径上面的参数值
     *
     * @param params 参数名称
     * @return 读取的参数名称对应的值
     */
    protected String getParamValue(ServerRequest request, String... params) {
        for (String param : params) {
            final Optional<String> optional = request.queryParam(param);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
        return null;
    }

    /**
     * 获取请求头部存在的令牌信息
     *
     * @return 头部的令牌信息
     */
    protected String getHeaderToken(ServerRequest request) {
        final HttpHeaders headers = request.headers().asHttpHeaders();
        final List<String> list = headers.get(AUTH_HEADER);
        if (list != null && list.size() > 0) {
            final String content = list.get(0);
            if (content.startsWith(AUTH_HEADER_TOKEN_PREFIX)) {
                return content.substring(AUTH_HEADER_TOKEN_PREFIX.length());
            }
        }
        return null;
    }

    /**
     * 结果统一处理
     *
     * @param accessToken  ACCESS TOKEN
     * @param refreshToken REFRESH TOKEN
     * @return 返回的结果对象
     */
    protected Map<String, Object> executeResultHandler(String accessToken, String refreshToken) {
        final Map<String, Object> result = new HashMap<>(4);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("expiration", EXPIRATION_TIME);
        result.put("type", AUTH_HEADER_TOKEN_TYPE);
        return result;
    }

}
