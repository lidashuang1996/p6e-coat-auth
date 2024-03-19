package club.p6e.coat.auth.client;

import club.p6e.coat.auth.client.cache.AuthCache;
import club.p6e.coat.auth.client.cache.AuthStateCache;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.common.controller.BaseWebController;
import club.p6e.coat.common.error.AuthStateException;
import club.p6e.coat.common.error.ParameterException;
import club.p6e.coat.common.utils.GeneratorUtil;
import club.p6e.coat.common.utils.HttpUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ResponseBody
@RequestMapping("/auth")
@ConditionalOnClass(name = "org.springframework.web.servlet.package-info")
public class AuthWebClientController extends BaseWebController {

    protected final AuthCache authCache;
    protected final Properties properties;
    protected final AuthStateCache authStateCache;

    @SuppressWarnings("ALL")
    protected final AuthJsonWebTokenCipher authJsonWebTokenCipher;

    public AuthWebClientController(
            Properties properties,
            AuthCache authCache,
            AuthStateCache authStateCache,
            AuthJsonWebTokenCipher authJsonWebTokenCipher
    ) {
        this.authCache = authCache;
        this.properties = properties;
        this.authStateCache = authStateCache;
        this.authJsonWebTokenCipher = authJsonWebTokenCipher;
    }

    @GetMapping("")
    public void def(HttpServletResponse response) {
        try {
            final String url = getParam("url");
            final String state = GeneratorUtil.random(8, true, false);
            authStateCache.set(state, url == null ? "" : url);
            response.sendRedirect(properties.getAuthorizeUrl()
                    + "?response_type=code"
                    + "&client_id=" + properties.getAuthorizeAppId()
                    + "&redirect_uri=" + properties.getAuthorizeAppRedirectUri()
                    + "&scope=open_id,user_info"
                    + "&state=" + state);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping("/callback")
    public ResultContext callback(HttpServletResponse response) {
        final String code = getParam("code");
        final String state = getParam("state");
        if (code != null && state != null) {
            final String cache = authStateCache.getAndDel(state);
            if (cache == null) {
                throw new AuthStateException(this.getClass(),
                        "fun callback().", "Request parameter state expires.");
            } else {
                final String tokenResult = HttpUtil.doPost(properties.getAuthorizeTokenUrl()
                        + "?code=" + code
                        + "&client_id=" + properties.getAuthorizeAppId()
                        + "&client_secret=" + properties.getAuthorizeAppSecret()
                        + "&redirect_uri=" + properties.getAuthorizeAppRedirectUri()
                );
                final String token = "!23";
                final String userInfoResult = HttpUtil.doGet(properties.getAuthorizeUserInfoUrl() + "?token=" + token);
                return authorization("123", "", response);
            }
        } else {
            throw new ParameterException(this.getClass(),
                    "fun callback().", "Request parameter code/state does not exist.");
        }
    }

    @SuppressWarnings("ALL")
    protected ResultContext authorization(String id, String info, HttpServletResponse response) {
        /*
        // JWT
        final Date date = Date.from(LocalDateTime.now().plusSeconds(3600L).atZone(ZoneId.systemDefault()).toInstant());
        final String accessToken = JWT.create().withAudience(String.valueOf(id)).withExpiresAt(date)
                .withSubject(info).sign(Algorithm.HMAC256(authJsonWebTokenCipher.getAccessTokenSecret()));
        final String refreshToken = JWT.create().withAudience(String.valueOf(id)).withExpiresAt(date)
                .withSubject(info).sign(Algorithm.HMAC256(authJsonWebTokenCipher.getRefreshTokenSecret()));
         */

        final String accessToken = GeneratorUtil.uuid() + GeneratorUtil.random();
        final String refreshToken = GeneratorUtil.uuid() + GeneratorUtil.random();
        authCache.set(id, "PC", accessToken, refreshToken, info);

        /*
        // COOKIES
        final Cookie accessTokenCookie = new Cookie("P6E_ACCESS_TOKEN", accessToken) {{
            setPath("/");
            setMaxAge(3600);
            setHttpOnly(true);
        }};
        final Cookie refreshTokenCookie = new Cookie("P6E_REFRESH_TOKEN", refreshToken) {{
            setPath("/");
            setMaxAge(3600);
            setHttpOnly(true);
        }};
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResultContext.build();
         */

        return ResultContext.build(new HashMap<>() {{
            put("accessToken", accessToken);
            put("refreshToken", refreshToken);
            put("tokenType", AUTH_HEADER_TOKEN_TYPE);
            put("expire", AuthCache.EXPIRATION_TIME);
        }});
    }


    protected void aaa() {

    }


    @Data
    @Accessors(chain = true)
    public static class TokenModel implements Serializable {
        private Integer code;
        private String message;
        private TokenDataModel data;
    }

    @Data
    @Accessors(chain = true)
    public static class TokenDataModel implements Serializable {
        private String data;
    }

    @Data
    @Accessors(chain = true)
    public static class UserInfoModel implements Serializable {
        private Integer code;
        private String message;
        private TokenDataModel data;
    }
}
