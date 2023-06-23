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
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthCertificateAuthorityHttpLocalStorageCacheImpl implements AuthCertificateAuthority {

    /**
     * 认证缓存对象
     */
    private final AuthCache cache;
    private final AuthAccessTokenGenerator accessTokenGenerator;
    private final AuthRefreshTokenGenerator refreshTokenGenerator;


    public AuthCertificateAuthorityHttpLocalStorageCacheImpl(
            AuthCache cache,
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator
    ) {
        this.cache = cache;
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    /**
     * 认证头类型
     */
    protected static final String AUTH_HEADER_TOKEN_TYPE = "Bearer";
    protected static final long EXPIRATION_TIME = 3600;

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        final String info = JsonUtil.toJson(user.toMap());
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String accessToken = accessTokenGenerator.execute();
                    final String refreshToken = refreshTokenGenerator.execute();
                    final Mono<Map<String, Object>> mono = cache
                            .set(user.id(), "", accessToken, refreshToken, info)
                            .map(t -> {
                                final Map<String, Object> result = new HashMap<>(5);
                                result.put("accessToken", t.getAccessToken());
                                result.put("refreshToken", t.getRefreshToken());
                                result.put("expiration", EXPIRATION_TIME);
                                result.put("type", AUTH_HEADER_TOKEN_TYPE);
                                return result;
                            });
                    final String oauth = v.get(AuthVoucher.OAUTH2);
                    System.out.println("oauthoauthoauthoauth >> " + oauth + "  " + StringUtils.hasText(oauth));
                    if (StringUtils.hasText(oauth)) {
                        return mono
                                .flatMap(m -> {
                                    final Map<String, String> map = new HashMap<>();
                                    map.put(AuthVoucher.OAUTH2_USER_ID, user.id());
                                    map.put(AuthVoucher.OAUTH2_USER_INFO, info);
                                    return v.set(map)
                                            .map(vv -> {
                                                final String clientId = v.get(AuthVoucher.OAUTH2_CLIENT_ID);
                                                final String clientName = v.get(AuthVoucher.OAUTH2_CLIENT_NAME);
                                                final String clientAvatar = v.get(AuthVoucher.OAUTH2_CLIENT_AVATAR);
                                                final String clientDescribe = v.get(AuthVoucher.OAUTH2_CLIENT_DESCRIBE);
                                                final String clientReconfirm = v.get(AuthVoucher.OAUTH2_CLIENT_RECONFIRM);
                                                final Map<String, Object> client = new HashMap<>(5);
                                                client.put("clientId", clientId);
                                                client.put("clientName", clientName);
                                                client.put("clientAvatar", clientAvatar);
                                                client.put("clientDescribe", clientDescribe);
                                                client.put("clientReconfirm", clientReconfirm);
                                                m.put("oauth2", client);
                                                return m;
                                            }).map(ResultContext::build);
                                });
                    } else {
                        return mono.map(ResultContext::build);
                    }
                });
    }

}
