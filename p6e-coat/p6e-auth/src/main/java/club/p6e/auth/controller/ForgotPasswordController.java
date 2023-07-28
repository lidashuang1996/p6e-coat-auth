package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/forgot/password")
public interface ForgotPasswordController<P, R> {

    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
