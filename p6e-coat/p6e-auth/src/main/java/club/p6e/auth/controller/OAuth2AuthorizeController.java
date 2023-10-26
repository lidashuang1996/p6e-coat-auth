package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 认证
 *
 * @param <P> 参数对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/authorize")
public interface OAuth2AuthorizeController<P> {

    /**
     * [GET]
     * OAuth2 认证页面
     *
     * @param param    请求对象
     * @param exchange ServerWebExchange 对象
     * @return 结果对象
     */
    @GetMapping("")
    public Mono<Void> execute(ServerWebExchange exchange, P param);

}
