package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/forgot/password")
public interface ForgotPasswordController<P, R> {

    /**
     * [POST]
     * 忘记密码
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
