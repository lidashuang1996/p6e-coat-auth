package club.p6e.auth.certificate;

import club.p6e.auth.AuthCertificateValidator;
import club.p6e.auth.AuthJsonWebTokenCipher;
import club.p6e.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class HttpLocalStorageJsonWebTokenCertificateValidator
        extends HttpCertificate implements AuthCertificateValidator {

    /**
     * 认证缓存的对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密码对象
     */
    public HttpLocalStorageJsonWebTokenCertificateValidator(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return getHttpLocalStorageToken(exchange.getRequest())
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun execute(ServerWebExchange exchange)",
                        "[HTTP/STORAGE/JWT] HTTP request access token does not exist."
                )))
                .flatMap(this::accessToken)
                .map(s -> exchange.mutate().request(
                        exchange.getRequest().mutate().header(USER_HEADER_NAME, s).build()
                ).build());
    }

    @Override
    public Mono<String> accessToken(String token) {
        final String r = jwtDecode(token, cipher.getAccessTokenSecret());
        return r == null ? Mono.error(GlobalExceptionContext.exceptionAuthException(
                this.getClass(),
                "fun accessToken(String token)",
                "[HTTP/STORAGE/JWT] Verifier validation access token exception."
        )) : Mono.just(r);
    }

    @Override
    public Mono<String> refreshToken(String token) {
        final String r = jwtDecode(token, cipher.getRefreshTokenSecret());
        return r == null ? Mono.error(GlobalExceptionContext.exceptionAuthException(
                this.getClass(),
                "fun refreshToken(String token)",
                "[HTTP/STORAGE/JWT] Verifier validation refresh token exception."
        )) : Mono.just(r);
    }

}
