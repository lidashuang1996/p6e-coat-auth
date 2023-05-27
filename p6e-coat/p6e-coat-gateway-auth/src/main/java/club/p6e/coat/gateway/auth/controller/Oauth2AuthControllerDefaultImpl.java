package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.Oauth2AuthService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * OAUTH2 认证的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2AuthController.class,
//        ignored = Oauth2AuthControllerDefaultImpl.class
//)
//@ConditionalOnExpression(Oauth2AuthController.CONDITIONAL_EXPRESSION)
public class Oauth2AuthControllerDefaultImpl
        implements Oauth2AuthController<Oauth2Context.Auth.Request, Object> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH2 认证的服务对象
     */
    private final Oauth2AuthService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     */
    public Oauth2AuthControllerDefaultImpl(Properties properties, Oauth2AuthService service) {
        this.service = service;
        this.properties = properties;
    }

    /**
     * 判断是否启用
     *
     * @return 是否启用
     */
    protected boolean isEnable() {
        return properties.getOauth2().isEnable()
                && properties.getOauth2().getAuthorizationCode().isEnable();
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, Oauth2Context.Auth.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, Oauth2Context.Auth.Request param) {
        return Mono
                .just(isEnable())
                // http://127.0.0.1:8080/oauth2/auth?responseType=code&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&scope=user_info&state=888
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, Oauth2Context.Auth.Request param)",
                                "Oauth2 auth service not enabled exception."
                        )))
                .flatMap(f -> service.execute(exchange, param));

    }

}
