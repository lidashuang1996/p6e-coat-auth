package club.p6e.coat.gateway.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 身份验证转换器
 * 默认采用的是外交部的签证的方式验证的令牌
 * 那是因为登录成功下发的令牌默认也是采用外交部签证的形式下发
 * 如果修改了登录成功下发令牌的方式，记得也要修改身份验证转换器服务对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ServerAuthenticationConverter.class,
        ignored = AuthForeignMinistryServerAuthenticationConverter.class
)
public class AuthForeignMinistryServerAuthenticationConverter implements ServerAuthenticationConverter {

    /**
     * 认证外交部对象
     */
    private final AuthForeignMinistry authForeignMinistry;

    /**
     * 构造方法初始化
     *
     * @param authForeignMinistry 认证外交部对象
     */
    public AuthForeignMinistryServerAuthenticationConverter(AuthForeignMinistry authForeignMinistry) {
        this.authForeignMinistry = authForeignMinistry;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return authForeignMinistry
                .verificationAccessToken(exchange.getRequest())
                .map(AuthForeignMinistryVisaTemplate::getAuthentication);
    }

}