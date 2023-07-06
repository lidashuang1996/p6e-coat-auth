package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateValidator;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateInterceptorHttpCookieJsonWebToken
        extends AuthCertificateHttp implements AuthCertificateValidator {

    /**
     * JWT 密码对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密码对象
     */
    public AuthCertificateInterceptorHttpCookieJsonWebToken(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpCookieAccessToken(exchange.getRequest())
                .map(t -> jwtRequire(t, cipher.getAccessTokenSecret()))
                .map(s -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(USER_HEADER_NAME, s).build()
                ).build());

    }

    @Override
    public Mono<String> accessToken(String token) {
        return Mono.just(jwtRequire(token, cipher.getAccessTokenSecret()));
    }

    @Override
    public Mono<String> refreshToken(String token) {
        return Mono.just(jwtRequire(token, cipher.getRefreshTokenSecret()));
    }

}
