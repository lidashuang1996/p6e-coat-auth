package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 INFO
 *
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/info")
public interface OAuth2InfoController<R> {

    /**
     * [GET]
     * OAuth2 用户信息
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("/user")
    Mono<R> user(ServerWebExchange exchange);


    /**
     * [GET]
     * OAuth2 客户端信息
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("/client")
    Mono<R> client(ServerWebExchange exchange);

}
