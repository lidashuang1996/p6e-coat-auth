package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.VerificationCodeObtainService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码获取实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = VerificationCodeObtainController.class,
//        ignored = VerificationCodeObtainControllerDefaultImpl.class
//)
//@ConditionalOnExpression(VerificationCodeObtainController.CONDITIONAL_EXPRESSION)
public class VerificationCodeObtainControllerImpl implements
        VerificationCodeObtainController<LoginContext.VerificationCodeObtain.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 验证码获取服务
     */
    private final VerificationCodeObtainService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    验证码获取的服务对象
     */
    public VerificationCodeObtainControllerImpl(Properties properties, VerificationCodeObtainService service) {
        this.service = service;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable();
    }

    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                                "Verification code login code obtain service not enabled exception."
                        )))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }
}
