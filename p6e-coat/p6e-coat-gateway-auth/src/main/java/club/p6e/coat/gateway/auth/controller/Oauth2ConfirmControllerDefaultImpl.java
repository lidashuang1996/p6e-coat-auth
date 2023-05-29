package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.Oauth2ConfirmService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * OAUTH2 CODE 模式
 * 进行认证的二次确认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2ConfirmController.class,
//        ignored = Oauth2ConfirmControllerDefaultImpl.class
//)
//@ConditionalOnExpression(Oauth2ConfirmController.CONDITIONAL_EXPRESSION)
public class Oauth2ConfirmControllerDefaultImpl
        implements Oauth2ConfirmController<Oauth2Context.Confirm.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH2 CODE 模式确认服务
     */
    private final Oauth2ConfirmService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    OAUTH2 CODE 模式确认的服务对象
     */
    public Oauth2ConfirmControllerDefaultImpl(Properties properties, Oauth2ConfirmService service) {
        this.service = service;
        this.properties = properties;
    }


    /**
     * 判断是否启用
     *
     * @return 是否启用
     */
    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable();
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
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, Oauth2Context.Confirm.Request param)",
                                "Oauth2 confirm service not enabled exception."
                        )))
                .flatMap(f -> service.execute(exchange, param))
                .map(ResultContext::build);
    }

}
