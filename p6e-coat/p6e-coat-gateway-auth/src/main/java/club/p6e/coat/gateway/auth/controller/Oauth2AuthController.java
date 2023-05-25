package club.p6e.coat.gateway.auth.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 认证
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/auth")
public interface Oauth2AuthController<P, R> {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.oauth2.enable:false} && ${p6e.auth.oauth2.authorization-code.enable:false}}";

    /**
     * [ POST/GET ]
     * 获取 OAUTH2 认证首页页面内容
     *
     * @param param 请求对象
     * @return 结果对象
     */
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public Mono<Void> execute(ServerWebExchange exchange, P param);

}
