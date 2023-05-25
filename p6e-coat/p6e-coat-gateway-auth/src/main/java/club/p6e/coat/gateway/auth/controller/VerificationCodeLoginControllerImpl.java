package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.VerificationCodeLoginService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = VerificationCodeLoginController.class,
//        ignored = VerificationCodeLoginControllerDefaultImpl.class
//)
//@ConditionalOnExpression(VerificationCodeLoginController.CONDITIONAL_EXPRESSION)
public class VerificationCodeLoginControllerImpl implements
        VerificationCodeLoginController<LoginContext.VerificationCode.Request, AuthUserDetails> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 验证码登录服务对象
     */
    private final VerificationCodeLoginService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    验证码登录的服务对象
     */
    public VerificationCodeLoginControllerImpl(Properties properties, VerificationCodeLoginService service) {
        this.service = service;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable();
    }

    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.VerificationCode.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.VerificationCode.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.VerificationCode.Request param)",
                                "Verification code login service not enabled exception."
                        )))
                .flatMap(service::execute);
    }
}
