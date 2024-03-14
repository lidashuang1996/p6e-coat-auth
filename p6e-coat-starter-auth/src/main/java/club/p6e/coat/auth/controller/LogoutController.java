package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 登出接口
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/logout")
public interface LogoutController {

    /**
     * [DELETE]
     * 登出接口
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @DeleteMapping("")
    public Mono<Void> execute(ServerWebExchange exchange);

}
