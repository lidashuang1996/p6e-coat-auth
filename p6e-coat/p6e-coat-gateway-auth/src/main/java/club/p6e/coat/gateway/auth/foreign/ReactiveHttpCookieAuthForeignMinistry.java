package club.p6e.coat.gateway.auth.foreign;

import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.List;

/**
 * 外交部抽象类
 * 通过 HTTP -> COOKIE 的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public abstract class ReactiveHttpCookieAuthForeignMinistry {

    /**
     * COOKIE 清除内容
     */
    protected static final String COOKIE_EMPTY_CONTENT = "";

    /**
     * COOKIE 清除时间
     */
    protected static final int COOKIE_EMPTY_EXPIRATION_TIME = 0;

    /**
     * 请求结果成功返回内容
     */
    protected static final String SUCCESS_RESULT = "SUCCESS";

    /**
     * COOKIE 过期时间
     * COOKIE 过期时间默认设置为 3600 秒
     */
    protected static int COOKIE_EXPIRATION_TIME = 3600;

    /**
     * AUTH COOKIE ACCESS TOKEN 名称
     */
    protected static String AUTH_COOKIE_ACCESS_TOKEN_NAME = "AUTH_ACCESS_TOKEN";

    /**
     * AUTH COOKIE REFRESH TOKEN 名称
     */
    protected static String AUTH_COOKIE_REFRESH_TOKEN_NAME = "AUTH_REFRESH_TOKEN";

    /**
     * 设置 COOKIE 过期时间
     *
     * @param expiration 设置的 COOKIE 过期时间
     */
    public static void setCookieExpirationTime(int expiration) {
        COOKIE_EXPIRATION_TIME = expiration;
    }

    /**
     * 设置 AUTH COOKIE ACCESS TOKEN 名称
     *
     * @param name 设置的 AUTH COOKIE ACCESS TOKEN 名称
     */
    public static void setAuthCookieAccessTokenName(String name) {
        AUTH_COOKIE_ACCESS_TOKEN_NAME = name;
    }

    /**
     * 设置 AUTH COOKIE REFRESH TOKEN 名称
     *
     * @param name 设置的 AUTH COOKIE REFRESH TOKEN 名称
     */
    public static void setAuthCookieRefreshTokenName(String name) {
        AUTH_COOKIE_REFRESH_TOKEN_NAME = name;
    }

    /**
     * 获取 COOKIE 对象
     *
     * @param request ServerRequest 对象
     * @param name    名称
     * @return COOKIE 对象
     */
    protected List<HttpCookie> getCookie(ServerHttpRequest request, String name) {
        final MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        final List<HttpCookie> list = cookies.get(name);
        return list == null || list.size() == 0 ? null : list;
    }

    /**
     * 获取 ACCESS TOKEN COOKIE 内容
     *
     * @param request HttpServletRequest 对象
     * @return ACCESS TOKEN COOKIE 内容
     */
    protected List<HttpCookie> getAccessTokenCookie(ServerHttpRequest request) {
        return getCookie(request, AUTH_COOKIE_ACCESS_TOKEN_NAME);
    }

    /**
     * 获取 REFRESH TOKEN COOKIE 内容
     *
     * @param request HttpServletRequest 对象
     * @return REFRESH TOKEN COOKIE 内容
     */
    protected List<HttpCookie> getRefreshTokenCookie(ServerHttpRequest request) {
        return getCookie(request, AUTH_COOKIE_REFRESH_TOKEN_NAME);
    }

}
