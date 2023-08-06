package club.p6e.auth.controller;

import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.ForgotPasswordService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordControllerImpl
        implements ForgotPasswordController<ForgotPasswordContext.Request, ResultContext> {

    private final ForgotPasswordService service;

    public ForgotPasswordControllerImpl(ForgotPasswordService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, ForgotPasswordContext.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, ForgotPasswordContext.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}
