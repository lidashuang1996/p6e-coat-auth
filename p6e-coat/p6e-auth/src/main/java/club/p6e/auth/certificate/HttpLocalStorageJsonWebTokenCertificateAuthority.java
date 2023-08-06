package club.p6e.auth.certificate;

import club.p6e.auth.AuthCertificateAuthority;
import club.p6e.auth.AuthJsonWebTokenCipher;
import club.p6e.auth.AuthUser;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证凭证下发（HttpLocalStorageJsonWebToken）
 *
 * @author lidashuang
 * @version 1.0
 */
public class HttpLocalStorageJsonWebTokenCertificateAuthority
        extends HttpCertificate implements AuthCertificateAuthority {

    /**
     * JWT 密码对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密码对象
     */
    public HttpLocalStorageJsonWebTokenCertificateAuthority(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<ResultContext> award(ServerWebExchange exchange, AuthUser.Model model) {
        final String uid = model.id();
        final String info = model.serialize();
        final String accessToken = jwtCreate(uid, info, cipher.getAccessTokenSecret());
        final String refreshToken = jwtCreate(uid, info, cipher.getRefreshTokenSecret());
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    if (v.isOAuth2()) {
                        final Map<String, Object> data = new HashMap<>(1);
                        data.put("oauth2", v.getOAuth2());
                        return v.setOAuth2User(uid, info)
                                .flatMap(vv -> setHttpLocalStorageToken(
                                        accessToken,
                                        refreshToken,
                                        data
                                ));
                    } else {
                        return v.del()
                                .flatMap(vv -> setHttpLocalStorageToken(
                                        accessToken,
                                        refreshToken
                                ));
                    }
                }).map(ResultContext::build);
    }

    @Override
    public Mono<Void> abolish(ServerWebExchange exchange) {
        return getHttpLocalStorageToken(exchange.getRequest())
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun abolish(ServerWebExchange exchange)",
                        "[HTTP/STORAGE/JWT] HTTP request access token does not exist."
                )))
                .flatMap(l -> Mono.empty());
    }

}
