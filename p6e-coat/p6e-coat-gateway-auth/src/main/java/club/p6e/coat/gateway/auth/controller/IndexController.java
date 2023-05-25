package club.p6e.coat.gateway.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 主页
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("")
public interface IndexController {

    @GetMapping("")
    public default Mono<Void> def1(ServerWebExchange exchange) {
        return this.execute(exchange);
    }

    @GetMapping("/")
    public default Mono<Void> def2(ServerWebExchange exchange) {
        return this.execute(exchange);
    }

    @GetMapping("/index")
    public default Mono<Void> def3(ServerWebExchange exchange) {
        return this.execute(exchange);
    }

    /**
     * 执行内容加载
     *
     * @param exchange ServerWebExchange 对象
     * @return Mono/Void 对象
     */
    public Mono<Void> execute(ServerWebExchange exchange);

}
