package club.p6e.cloud.auth.controller;

import club.p6e.cloud.auth.context.Oauth2Context;
import club.p6e.cloud.auth.context.ResultContext;
import club.p6e.cloud.auth.service.Oauth2ConfirmService;
import club.p6e.cloud.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 CODE 模式
 * 进行认证的二次确认实现
 *
 * @author lidashuang
 * @version 1.0
 */
//@ConditionalOnMissingBean(
//        value = Oauth2ConfirmController.class,
//        ignored = Oauth2ConfirmControllerDefaultImpl.class
//)
public class Oauth2ConfirmControllerImpl
        implements Oauth2ConfirmController<Oauth2Context.Confirm.Request, ResultContext> {

    /**
     * OAUTH2 CODE 模式确认服务
     */
    private final Oauth2ConfirmService service;

    /**
     * 构造方法
     *
     * @param service OAUTH2 CODE 模式确认的服务对象
     */
    public Oauth2ConfirmControllerImpl(Oauth2ConfirmService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, Oauth2Context.Confirm.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, Oauth2Context.Confirm.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(f -> service.execute(exchange, param))
                .map(ResultContext::build);
    }

}
