package club.p6e.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录获取
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/login/qrcode/obtain")
public interface QrCodeObtainController<P, R> {

    /**
     * [GET]
     * 二维码获取
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    @GetMapping("")
    public Mono<R> execute(ServerWebExchange exchange, P param);

}

