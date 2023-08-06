package club.p6e.auth.certificate;

import club.p6e.auth.AuthCertificateAuthority;
import club.p6e.auth.AuthUser;
import club.p6e.auth.cache.AuthCache;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.generator.AuthAccessTokenGenerator;
import club.p6e.auth.generator.AuthRefreshTokenGenerator;
import club.p6e.auth.AuthVoucher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证凭证下发（HttpCookieCache）
 *
 * @author lidashuang
 * @version 1.0
 */
public class HttpCookieCacheCertificateAuthority
        extends HttpCertificate implements AuthCertificateAuthority {

    /**
     * 认证缓存
     */
    protected final AuthCache cache;

    /**
     * ACCESS TOKEN 生成器
     */
    protected final AuthAccessTokenGenerator accessTokenGenerator;

    /**
     * REFRESH TOKEN 生成器
     */
    protected final AuthRefreshTokenGenerator refreshTokenGenerator;

    /**
     * 构造方法初始化
     *
     * @param cache                 认证缓存
     * @param accessTokenGenerator  ACCESS TOKEN 生成器
     * @param refreshTokenGenerator REFRESH TOKEN 生成器
     */
    public HttpCookieCacheCertificateAuthority(
            AuthCache cache,
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator
    ) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public Mono<ResultContext> award(ServerWebExchange exchange, AuthUser.Model model) {
        final String uid = model.id();
        final String info = model.serialize();
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> cache
                        .set(uid, v.device(), accessToken, refreshToken, info)
                        .flatMap(t -> {
                            if (v.isOAuth2()) {
                                final Map<String, Object> data = new HashMap<>(1);
                                data.put("oauth2", v.getOAuth2());
                                return v.setOAuth2User(uid, info)
                                        .flatMap(vv -> setHttpCookieToken(
                                                exchange.getResponse(),
                                                t.getAccessToken(),
                                                t.getRefreshToken(),
                                                data
                                        ));
                            } else {
                                return v.del()
                                        .flatMap(vv -> setHttpCookieToken(
                                                exchange.getResponse(),
                                                t.getAccessToken(),
                                                t.getRefreshToken()
                                        ));
                            }
                        })).map(ResultContext::build);
    }

    @Override
    public Mono<Void> abolish(ServerWebExchange exchange) {
        return getHttpCookieToken(exchange.getRequest())
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun abolish(ServerWebExchange exchange)",
                        "[HTTP/COOKIE/CACHE] HTTP request access token does not exist."
                )))
                .flatMap(cache::getAccessToken)
                .flatMap(t -> cache.cleanToken(t.getAccessToken()))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAuthException(
                        this.getClass(),
                        "fun abolish(ServerWebExchange exchange)",
                        "[HTTP/COOKIE/CACHE] Verifier validation clean access token exception."
                )))
                .flatMap(l -> cleanHttpCookieToken(exchange.getResponse()));
    }

}
