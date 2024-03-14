package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注册的接口
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/register")
public interface RegisterController<P, R> {

    /**
     * [GET]
     * 注册页面
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("")
    public Mono<Void> def(ServerWebExchange exchange);

    /**
     * [POST]
     * 注册的接口
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
