package club.p6e.coat.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/login/qrcode")
public interface QrCodeLoginController<P, R> {

    /**
     * [POST]
     * 二维码登录
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @PostMapping("")
    Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}

