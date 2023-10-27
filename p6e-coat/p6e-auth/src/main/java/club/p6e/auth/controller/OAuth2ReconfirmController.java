package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 CODE 模式
 * 进行认证的二次确认
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/reconfirm")
public interface OAuth2ReconfirmController<P, R> {

    /**
     * [GET]
     * 确认页面
     *
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("")
    public Mono<Void> def(ServerWebExchange exchange);

    /**
     * [POST]
     * OAuth2 二次确认
     *
     * @param param    请求对象
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @PostMapping("/save")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
