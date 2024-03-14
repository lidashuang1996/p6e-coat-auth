package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthCertificateAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 退出登录的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class LogoutControllerImpl implements LogoutController {

    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 构造方法初始化
     *
     * @param authority 认证授权的服务对象
     */
    public LogoutControllerImpl(AuthCertificateAuthority authority) {
        this.authority = authority;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange) {
        return authority.abolish(exchange);
    }

}
