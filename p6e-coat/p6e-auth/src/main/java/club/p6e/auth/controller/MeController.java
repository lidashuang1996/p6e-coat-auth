package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/me")
public interface MeController<R> {

    @GetMapping("")
    public Mono<Void> def(ServerWebExchange exchange);

    @GetMapping("/info")
    public Mono<R> info(ServerWebExchange exchange);

}
