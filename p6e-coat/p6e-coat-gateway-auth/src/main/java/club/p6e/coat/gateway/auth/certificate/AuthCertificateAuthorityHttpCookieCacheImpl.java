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
            AuthAccessTokenGenerator accessTokenGenerator,
            AuthRefreshTokenGenerator refreshTokenGenerator
    ) {
        this.accessTokenGenerator = accessTokenGenerator;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        // 序列化用户的信息
        final String info = JsonUtil.toJson(user.toMap());
        // 生成令牌
        final String accessToken = accessTokenGenerator.execute();
        final String refreshToken = refreshTokenGenerator.execute();
        return resultHttpLocalStorage(
                user.id(),
                info,
                accessToken,
                refreshToken,
                exchange
        ).map(ResultContext::build);
    }

}
