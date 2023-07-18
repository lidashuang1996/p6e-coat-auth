package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.Oauth2TokenService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;

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
public class Oauth2TokenControllerDefaultImpl
        implements Oauth2TokenController<Oauth2Context.Token.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH2 TOKEN 服务
     */
    private final Oauth2TokenService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    OAUTH2 TOKEN 服务
     */
    public Oauth2TokenControllerDefaultImpl(Properties properties, Oauth2TokenService service) {
        this.service = service;
        this.properties = properties;
    }

    /**
     * 判断是否启用
     *
     * @return 是否启用
     */
    protected boolean isEnable() {
        return properties.getOauth2().isEnable();
    }

    protected Mono<Void> vp(ServerWebExchange exchange, Oauth2Context.Token.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, Oauth2Context.Token.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, Oauth2Context.Token.Request param)",
                                "Oauth2 token service not enabled exception."
                        )))
                .flatMap(f -> service.execute(exchange, param))
                .map(ResultContext::build);
    }

}
