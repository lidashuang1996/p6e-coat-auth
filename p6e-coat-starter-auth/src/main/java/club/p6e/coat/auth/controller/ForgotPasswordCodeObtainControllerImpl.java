package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.context.ForgotPasswordContext;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.service.ForgotPasswordObtainService;
import club.p6e.coat.auth.validator.ParameterValidator;
import club.p6e.coat.common.utils.CopyUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码验证码获取的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordCodeObtainControllerImpl
        implements ForgotPasswordCodeObtainController<ForgotPasswordContext.CodeObtain.Request, ResultContext> {

    /**
     * 忘记密码验证码获取的服务
     */
    private final ForgotPasswordObtainService service;

    /**
     * 构造方法初始化
     *
     * @param service 忘记密码验证码获取的服务
     */
    public ForgotPasswordCodeObtainControllerImpl(ForgotPasswordObtainService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, ForgotPasswordContext.CodeObtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, ForgotPasswordContext.CodeObtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(r -> ResultContext.build(CopyUtil.run(r, ForgotPasswordContext.CodeObtain.Vo.class)));
    }

}
