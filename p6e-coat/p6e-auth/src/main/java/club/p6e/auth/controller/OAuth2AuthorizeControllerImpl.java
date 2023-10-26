package club.p6e.auth.controller;

import club.p6e.auth.context.OAuth2Context;
import club.p6e.auth.service.Oauth2AuthService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 认证的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class OAuth2AuthorizeControllerImpl
        implements OAuth2AuthorizeController<OAuth2Context.Auth.Request> {

    /**
     * OAUTH2 认证的服务对象
     */
    private final Oauth2AuthService service;

    /**
     * 构造方法
     *
     * @param service OAUTH2 认证的服务对象
     */
    public OAuth2AuthorizeControllerImpl(Oauth2AuthService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, OAuth2Context.Auth.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, OAuth2Context.Auth.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(f -> service.execute(exchange, param));
    }

}
