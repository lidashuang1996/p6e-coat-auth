package club.p6e.coat.auth.service;

import club.p6e.coat.auth.context.RegisterContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface RegisterObtainService {
    Mono<RegisterContext.Obtain.Dto> execute(ServerWebExchange exchange, RegisterContext.Obtain.Request p);
}
