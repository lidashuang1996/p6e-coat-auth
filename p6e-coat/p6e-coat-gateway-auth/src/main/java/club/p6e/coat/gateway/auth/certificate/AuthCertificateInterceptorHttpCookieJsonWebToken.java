package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateInterceptorHttpCookieJsonWebToken
        extends AuthCertificateInterceptorBaseHttp implements AuthCertificateInterceptor {

    public AuthCertificateInterceptorHttpCookieJsonWebToken() {
    }

//    @Override
//    public Mono<Object> use(ServerWebExchange exchange, AuthUserDetails user) {
//        final String accessToken = accessTokenGenerator.execute();
//        final String refreshToken = refreshTokenGenerator.execute();
//        return cache
//                .set(String.valueOf(user.getId()), "PC",
//                        accessToken, refreshToken, JsonUtil.toJson(user.toMap()))
//                .switchIfEmpty(Mono.error(GlobalExceptionContext
//                        .executeCacheException(
//                                this.getClass(),
//                                "fun use(ServerWebExchange exchange, AuthUserDetails user)",
//                                "Authentication cache write exception."
//                        )))
//                .map(t -> {
//                    final Map<String, Object> r = new HashMap<>();
//                    r.put("accessToken", accessToken);
//                    r.put("refreshToken", refreshToken);
//                    r.put("expiration", EXPIRATION_TIME);
//                    r.put("type", AUTH_HEADER_TOKEN_TYPE);
//                    return r;
//                });
//    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return null;
    }

}
