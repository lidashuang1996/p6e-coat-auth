package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码验证码获取
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/forgot/password/obtain")
public interface ForgotPasswordCodeObtainController<P, R> {

    /**
     * [GET]
     * 忘记密码验证码获取
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @GetMapping("")
    Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
