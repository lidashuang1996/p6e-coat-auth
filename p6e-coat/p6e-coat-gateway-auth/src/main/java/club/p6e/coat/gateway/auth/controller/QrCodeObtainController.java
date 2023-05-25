package club.p6e.coat.gateway.auth.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    @PostMapping("")
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}

