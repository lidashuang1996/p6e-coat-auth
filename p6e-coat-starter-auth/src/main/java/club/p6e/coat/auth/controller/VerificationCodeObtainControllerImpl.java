package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.context.LoginContext;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.service.VerificationCodeObtainService;
import club.p6e.coat.auth.validator.ParameterValidator;
import club.p6e.coat.common.utils.CopyUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码获取的实现
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
     * 构造方法初始化
     *
     * @param service 验证码获取的服务对象
     */
    public VerificationCodeObtainControllerImpl(VerificationCodeObtainService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(r -> ResultContext.build(CopyUtil.run(r, LoginContext.VerificationCodeObtain.Vo.class)));
    }
}
