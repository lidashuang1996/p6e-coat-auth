package club.p6e.coat.gateway.auth.certificate;

import club.p6e.coat.gateway.auth.AuthCertificateAuthority;
import club.p6e.coat.gateway.auth.AuthJsonWebTokenCipher;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * 认证凭证下发（HttpLocalStorageJsonWebToken）
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthCertificateAuthorityHttpLocalStorageJsonWebTokenImpl
        extends AuthCertificateHttp implements AuthCertificateAuthority {

    /**
     * JWT 内容
     */
    private static final String CONTENT = "content";

    /**
     * 认证缓存的对象
     */
    protected final AuthJsonWebTokenCipher cipher;

    /**
     * 构造方法初始化
     *
     * @param cipher JWT 密码对象
     */
    public AuthCertificateAuthorityHttpLocalStorageJsonWebTokenImpl(AuthJsonWebTokenCipher cipher) {
        this.cipher = cipher;
    }

    @Override
    public Mono<Object> present(ServerWebExchange exchange, AuthUser user) {
        // 序列化用户的信息
        final String info = JsonUtil.toJson(user.toMap());
        final Date date = new Date(LocalDateTime.now()
                .plusSeconds(EXPIRATION_TIME)
                .toInstant(ZoneOffset.of("+8"))
                .toEpochMilli()
        );
        final String accessToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(user.id())
                // 设置签名内容
                .withClaim(CONTENT, JsonUtil.toJson(user.toMap()))
                // 使用HMAC算法
                .sign(Algorithm.HMAC256(cipher.getAccessTokenSecret()));
        final String refreshToken = JWT.create()
                // 设置过期时间
                .withExpiresAt(date)
                // 设置接受方信息
                .withAudience(user.id())
                // 设置签名内容
                .withClaim(CONTENT, JsonUtil.toJson(user.toMap()))
                // 使用HMAC算法
                .sign(Algorithm.HMAC256(cipher.getRefreshTokenSecret()));
        return resultHttpLocalStorage(
                user.id(),
                info,
                accessToken,
                refreshToken,
                exchange
        ).map(ResultContext::build);
    }

}
