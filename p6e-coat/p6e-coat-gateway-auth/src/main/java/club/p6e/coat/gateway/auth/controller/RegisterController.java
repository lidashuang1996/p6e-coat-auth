package club.p6e.coat.gateway.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface RegisterController<P, R> {

    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
