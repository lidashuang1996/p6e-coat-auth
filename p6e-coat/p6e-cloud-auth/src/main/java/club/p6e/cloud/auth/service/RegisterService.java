package club.p6e.cloud.auth.service;

import club.p6e.cloud.auth.context.RegisterContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface RegisterService {
    Mono<? extends RegisterContext.Dto> execute(ServerWebExchange exchange, RegisterContext.Request p);
}
