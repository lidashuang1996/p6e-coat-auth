package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/register")
public interface RegisterController<P, R> {

    @GetMapping("")
    public Mono<Void> def(ServerWebExchange exchange);

    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
