package club.p6e.auth.controller;

import club.p6e.auth.context.Oauth2Context;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.Oauth2TokenService;
import club.p6e.auth.validator.ParameterValidator;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


/**
 * OAUTH2 TOKEN
 * 进行认证的进一步操作
 *
 * @author lidashuang
 * @version 1.0
 */
//@ConditionalOnMissingBean(
//        value = Oauth2TokenController.class,
//        ignored = Oauth2TokenControllerDefaultImpl.class
//)
public class Oauth2TokenControllerImpl
        implements Oauth2TokenController<Oauth2Context.Token.Request, ResultContext> {

    /**
     * OAUTH2 TOKEN 服务
     */
    private final Oauth2TokenService service;

    /**
     * 构造方法
     *
     * @param service OAUTH2 TOKEN 服务
     */
    public Oauth2TokenControllerImpl(Oauth2TokenService service) {
        this.service = service;
    }

    protected Mono<Void> vp(ServerWebExchange exchange, Oauth2Context.Token.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, Oauth2Context.Token.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(f -> service.execute(exchange, param))
                .map(ResultContext::build);
    }

}
