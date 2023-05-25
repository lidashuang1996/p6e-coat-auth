package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUser;
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
public interface QrCodeLoginController<P, R extends AuthUser> {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 执行操作
     *
     * @param param 请求对象
     * @return 结果对象
     */
    @PostMapping
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}

