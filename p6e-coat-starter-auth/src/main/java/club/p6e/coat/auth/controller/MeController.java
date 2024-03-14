package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 我的信息接口
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/me")
public interface MeController<R> {

    /**
     * [GET]
     * 我的信息页面
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("")
    public Mono<Void> def(ServerWebExchange exchange);

    /**
     * [GET]
     * 我的信息接口
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("/info")
    public Mono<R> info(ServerWebExchange exchange);

}
