package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateAuthority;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.cache.AuthCache;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.generator.AuthAccessTokenGenerator;
import club.p6e.coat.gateway.auth.generator.AuthRefreshTokenGenerator;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证凭证下发（HttpLocalStorageCache）
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateAuthorityHttpLocalStorageCacheImpl
        extends AuthCertificateHttp implements AuthCertificateAuthority {

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
     * @param accessTokenGenerator  ACCESS TOKEN 生成器
     * @param refreshTokenGenerator REFRESH TOKEN 生成器
     */
    public AuthCertificateAuthorityHttpLocalStorageCacheImpl(
            AuthCache cache,
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator
    ) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        final String uid = user.id();
        final String info = JsonUtil.toJson(user.toMap());
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> cache
                        .set(uid, "", accessToken, refreshToken, info)
                        .flatMap(t -> {
                            final String oauth = v.get(AuthVoucher.OAUTH2);
                            if (StringUtils.hasText(oauth)) {
                                final Map<String, String> map = new HashMap<>();
                                final Map<String, Object> data = new HashMap<>();
                                map.put(AuthVoucher.OAUTH2_USER_ID, uid);
                                map.put(AuthVoucher.OAUTH2_USER_INFO, info);
                                data.put("oauth2", v.client());
                                return v
                                        .set(map)
                                        .flatMap(vv -> setHttpLocalStorageToken(
                                                t.getAccessToken(),
                                                t.getRefreshToken(),
                                                data
                                        ));
                            } else {
                                return v
                                        .del()
                                        .flatMap(vv -> setHttpLocalStorageToken(
                                                t.getAccessToken(),
                                                t.getRefreshToken()
                                        ));
                            }
                        }))
                .map(ResultContext::build);
    }

}
