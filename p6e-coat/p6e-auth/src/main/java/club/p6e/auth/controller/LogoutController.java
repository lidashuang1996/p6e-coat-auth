package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证登录
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/logout")
public interface LogoutController {

    @DeleteMapping("")
    public Mono<Void> execute(ServerWebExchange exchange);

}
