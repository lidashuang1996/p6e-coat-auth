package club.p6e.cloud.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 INFO
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/info")
public interface Oauth2InfoController<R> {

    @PostMapping("/user")
    public Mono<R> user(ServerWebExchange exchange);

    @PostMapping("/client")
    public Mono<R> client(ServerWebExchange exchange);

}
