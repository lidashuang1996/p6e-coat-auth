package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.context.OAuth2Context;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.service.OAuth2ReconfirmService;
import club.p6e.coat.auth.validator.ParameterValidator;
import club.p6e.coat.common.utils.CopyUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 CODE 模式
 * 进行认证的二次确认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class OAuth2ReconfirmControllerImpl
        implements OAuth2ReconfirmController<OAuth2Context.Confirm.Request, ResultContext> {

    /**
     * OAUTH2 CODE 模式确认服务
     */
    private final OAuth2ReconfirmService service;

    /**
     * 构造方法
     *
     * @param service OAUTH2 CODE 模式确认的服务对象
     */
    public OAuth2ReconfirmControllerImpl(OAuth2ReconfirmService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, OAuth2Context.Confirm.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<Void> def(ServerWebExchange exchange) {
        return service.def(exchange);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, OAuth2Context.Confirm.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(f -> service.execute(exchange, param))
                .map(r -> ResultContext.build(CopyUtil.run(r, OAuth2Context.Confirm.Vo.class)));
    }

}
