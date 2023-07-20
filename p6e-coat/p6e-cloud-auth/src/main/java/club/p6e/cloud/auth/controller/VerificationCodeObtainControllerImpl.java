package club.p6e.cloud.auth.controller;

import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.context.ResultContext;
import club.p6e.cloud.auth.service.VerificationCodeObtainService;
import club.p6e.cloud.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码获取实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeObtainControllerImpl implements
        VerificationCodeObtainController<LoginContext.VerificationCodeObtain.Request, ResultContext> {

    /**
     * 验证码获取服务
     */
    private final VerificationCodeObtainService service;

    /**
     * 构造方法
     *
     * @param service    验证码获取的服务对象
     */
    public VerificationCodeObtainControllerImpl(VerificationCodeObtainService service) {
        this.service = service;
    }

    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }
}
