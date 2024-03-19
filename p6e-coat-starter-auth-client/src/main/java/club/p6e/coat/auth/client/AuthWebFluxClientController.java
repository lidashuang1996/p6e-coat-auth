package club.p6e.coat.auth.client;

import club.p6e.coat.auth.client.cache.AuthCache;
import club.p6e.coat.auth.client.cache.AuthCacheReactive;
import club.p6e.coat.auth.client.cache.AuthStateCacheReactive;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.common.controller.BaseWebFluxController;
import club.p6e.coat.common.error.AuthStateException;
import club.p6e.coat.common.error.ParameterException;
import club.p6e.coat.common.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.HashMap;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ResponseBody
@RequestMapping("/auth")
@ConditionalOnClass(name = "org.springframework.web.reactive.package-info")
public class AuthWebFluxClientController extends BaseWebFluxController {

    protected final Properties properties;
    protected final AuthCacheReactive authCache;
    protected final AuthStateCacheReactive authStateCache;

    @SuppressWarnings("ALL")
    protected final AuthJsonWebTokenCipher authJsonWebTokenCipher;

    public AuthWebFluxClientController(
            Properties properties,
            AuthCacheReactive authCache,
            AuthStateCacheReactive authStateCache,
            AuthJsonWebTokenCipher authJsonWebTokenCipher
    ) {
        this.authCache = authCache;
        this.properties = properties;
        this.authStateCache = authStateCache;
        this.authJsonWebTokenCipher = authJsonWebTokenCipher;
    }


    @GetMapping("")
    public Mono<Void> def(ServerHttpRequest request, ServerHttpResponse response) {
        final String url = getParam(request, "url");
        final String state = GeneratorUtil.random(8, true, false);
        return authStateCache
                .set(state, url == null ? "" : url)
                .flatMap(b -> {
                    if (b) {
                        response.setStatusCode(HttpStatus.FOUND);
                        response.getHeaders().setLocation(URI.create(properties.getAuthorizeUrl()
                                + "?response_type=code"
                                + "&client_id=" + properties.getAuthorizeAppId()
                                + "&redirect_uri=" + properties.getAuthorizeAppRedirectUri()
                                + "&scope=open_id,user_info"
                                + "&state=" + state));
                        return response.setComplete();
                    } else {
                        return Mono.empty();
                    }
                });
    }

    @RequestMapping("/callback")
    public Mono<ResultContext> callback(ServerHttpRequest request, ServerHttpResponse response) {
        final String code = getParam(request, "code");
        final String state = getParam(request, "state");
        if (code != null && state != null) {
            return authStateCache.getAndDel(state)
                    .flatMap(cache -> {
                        if (cache == null) {
                            return Mono.error(new AuthStateException(this.getClass(),
                                    "fun callback().", "Request parameter state expires."));
                        } else {
//                            final String tokenResult = HttpUtil.doPost(properties.getAuthorizeTokenUrl()
//                                    + "?code=" + code
//                                    + "&client_id=" + properties.getAuthorizeAppId()
//                                    + "&client_secret=" + properties.getAuthorizeAppSecret()
//                                    + "&redirect_uri=" + properties.getAuthorizeAppRedirectUri()
//                            );
//                            final String token = "!23";
//                            final String userInfoResult = HttpUtil.doGet(properties.getAuthorizeUserInfoUrl() + "?token=" + token);
//                            return ;
                            return Mono.just(authorization("123", "", response));
                        }
                    });
        } else {
            return Mono.error(new ParameterException(this.getClass(),
                    "fun callback().", "Request parameter code/state does not exist."));
        }
    }

    @SuppressWarnings("ALL")
    protected ResultContext authorization(String id, String info, ServerHttpResponse response) {
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
        final ResponseCookie accessTokenCookie = ResponseCookie.from("P6E_ACCESS_TOKEN", accessToken)
                .maxAge(Duration.ofSeconds(3600L)).path("/").httpOnly(true).build();
        final ResponseCookie refreshTokenCookie = ResponseCookie.from("P6E_REFRESH_TOKEN", refreshToken)
                .maxAge(Duration.ofSeconds(3600L)).path("/").httpOnly(true).build();
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

}
