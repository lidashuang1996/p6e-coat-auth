package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.context.ResultContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface QqOtherLoginService {

    public Mono<String> home(ServerWebExchange exchange);

    public Mono<AuthUser.Model> callback(ServerWebExchange exchange);

}
