package club.p6e.auth.controller;

import club.p6e.auth.AuthCertificateAuthority;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class LogoutControllerImpl implements LogoutController {

    private final AuthCertificateAuthority authority;

    public LogoutControllerImpl(AuthCertificateAuthority authority) {
        this.authority = authority;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange) {
        return authority.revoke(exchange);
    }

}
