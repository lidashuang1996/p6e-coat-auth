package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.context.OAuth2Context;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.service.OAuth2TokenService;
import club.p6e.coat.auth.validator.ParameterValidator;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * OAUTH2 TOKEN
 * 进行认证的进一步操作
 *
 * @author lidashuang
 * @version 1.0
 */
public class OAuth2TokenControllerImpl
        implements OAuth2TokenController<OAuth2Context.Token.Request, ResultContext> {

    /**
     * OAUTH2 TOKEN 服务
     */
    private final OAuth2TokenService service;

    /**
     * 构造方法
     *
     * @param service OAUTH2 TOKEN 服务
     */
    public OAuth2TokenControllerImpl(OAuth2TokenService service) {
        this.service = service;
    }

    protected Mono<Void> vp(ServerWebExchange exchange, OAuth2Context.Token.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, OAuth2Context.Token.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(f -> service.execute(exchange, param))
                .map(ResultContext::build);
    }

}
