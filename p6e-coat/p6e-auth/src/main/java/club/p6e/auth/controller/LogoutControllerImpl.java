package club.p6e.auth.controller;

import club.p6e.auth.AuthCertificateAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class LogoutControllerImpl implements LogoutController {

    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 构造方法
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
