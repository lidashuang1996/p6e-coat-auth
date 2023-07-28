package club.p6e.auth.service;

import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.context.RegisterContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface ForgotPasswordService {

    Mono<ForgotPasswordContext.Dto> execute(ServerWebExchange exchange, ForgotPasswordContext.Request param);

}
