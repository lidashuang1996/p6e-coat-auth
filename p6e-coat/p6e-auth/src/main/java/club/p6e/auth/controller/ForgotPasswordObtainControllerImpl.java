package club.p6e.auth.controller;

import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.ForgotPasswordObtainService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码验证码获取的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordObtainControllerImpl
        implements ForgotPasswordObtainController<ForgotPasswordContext.Obtain.Request, ResultContext> {

    /**
     * 忘记密码验证码获取的服务
     */
    private final ForgotPasswordObtainService service;

    /**
     * 构造方法
     *
     * @param service 忘记密码验证码获取的服务
     */
    public ForgotPasswordObtainControllerImpl(ForgotPasswordObtainService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}
