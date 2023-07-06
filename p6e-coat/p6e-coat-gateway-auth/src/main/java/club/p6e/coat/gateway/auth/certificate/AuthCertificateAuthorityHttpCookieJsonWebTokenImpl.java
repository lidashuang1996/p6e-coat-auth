package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateAuthority;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证凭证下发（HttpCookieJsonWebToken）
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateAuthorityHttpCookieJsonWebTokenImpl
        extends AuthCertificateHttp implements AuthCertificateAuthority {

    /**
     * 认证缓存的对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密码对象
     */
    public AuthCertificateAuthorityHttpCookieJsonWebTokenImpl(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        final String uid = user.id();
        final String info = JsonUtil.toJson(user.toMap());
        final String accessToken = jwtCreate(uid, info, cipher.getAccessTokenSecret());
        final String refreshToken = jwtCreate(uid, info, cipher.getRefreshTokenSecret());
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String oauth = v.get(AuthVoucher.OAUTH2);
                    if (StringUtils.hasText(oauth)) {
                        final Map<String, String> map = new HashMap<>();
                        map.put(AuthVoucher.OAUTH2_USER_ID, uid);
                        map.put(AuthVoucher.OAUTH2_USER_INFO, info);
                        return v
                                .set(map)
                                .flatMap(vv -> setHttpCookieToken(
                                        exchange.getResponse(),
                                        accessToken,
                                        refreshToken,
                                        v.client()
                                ));
                    } else {
                        return v
                                .del()
                                .flatMap(vv -> setHttpCookieToken(
                                        exchange.getResponse(),
                                        accessToken,
                                        refreshToken
                                ));
                    }
                }).map(ResultContext::build);
    }

}
