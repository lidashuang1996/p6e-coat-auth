package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/login/account")
public interface AccountPasswordLoginController<P, R extends AuthUser> {

    /**
     * 条件注册的条件表达式
     */
    String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.account-password.enable:false}}";

    /**
     * [ POST ]
     * JSON/BODY
     * 账号密码登录的操作
     *
     * @param param 请求对象
     * @return Mono/R 结果对象
     */
    @PostMapping()
    public Mono<R> execute(ServerWebExchange exchange, @RequestBody P param);

}
