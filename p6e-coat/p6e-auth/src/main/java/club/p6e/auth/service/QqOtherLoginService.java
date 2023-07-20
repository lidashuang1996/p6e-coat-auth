package club.p6e.auth.service;

import club.p6e.auth.AuthUser;
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
