package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateInterceptorHttpLocalStorageJsonWebToken implements AuthCertificateInterceptor {

    /**
     * 用户信息的头部名称
     */
    protected static final String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 认证缓存的对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param jwt JWT 对象
     */
    public AuthCertificateInterceptorHttpLocalStorageJsonWebToken(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<ServerWebExchange> execute(ServerWebExchange exchange) {
        return Mono.just(exchange);
    }

}
