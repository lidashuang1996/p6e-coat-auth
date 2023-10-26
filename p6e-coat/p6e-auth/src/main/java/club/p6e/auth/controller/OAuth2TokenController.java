package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 TOKEN
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/token")
public interface OAuth2TokenController<P, R> {

    /**
     * [POST]
     * TOKEN
     *
     * @param param    请求对象
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
