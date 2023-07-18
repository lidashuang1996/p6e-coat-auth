package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateAuthority;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.context.ResultContext;
import org.springframework.util.StringUtils;
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
    public Mono<ResultContext> present(ServerWebExchange exchange, AuthUser.Model model) {
        final String uid = model.id();
        final String info = model.serialize();
        final String accessToken = jwtCreate(uid, info, cipher.getAccessTokenSecret());
        final String refreshToken = jwtCreate(uid, info, cipher.getRefreshTokenSecret());
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String oauth = v.get(AuthVoucher.OAUTH2);
                    if (StringUtils.hasText(oauth)) {
                        final Map<String, Object> data = new HashMap<>(1);
                        data.put("oauth2", v.oauth2());
                        final Map<String, String> map = new HashMap<>(2);
                        map.put(AuthVoucher.OAUTH2_USER_ID, uid);
                        map.put(AuthVoucher.OAUTH2_USER_INFO, info);
                        return v
                                .set(map)
                                .flatMap(vv -> setHttpLocalStorageToken(
                                        accessToken,
                                        refreshToken,
                                        data
                                ));
                    } else {
                        return v
                                .del()
                                .flatMap(vv -> setHttpLocalStorageToken(
                                        accessToken,
                                        refreshToken
                                ));
                    }
                }).map(ResultContext::build);
    }

}
