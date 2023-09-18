package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注册的验证码获取接口
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/register/obtain")
public interface RegisterObtainController<P, R> {

    /**
     * [POST]
     * 注册的验证码接口
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}


