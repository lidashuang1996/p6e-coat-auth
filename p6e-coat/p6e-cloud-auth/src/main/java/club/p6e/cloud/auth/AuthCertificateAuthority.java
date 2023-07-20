package club.p6e.cloud.auth;

import club.p6e.cloud.auth.context.ResultContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthCertificateAuthority {
    public Mono<ResultContext> present(ServerWebExchange exchange, AuthUser.Model model);
}
