package club.p6e.auth.controller;

import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.RegisterObtainService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 注册的验证码获取的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class RegisterObtainControllerImpl
        implements RegisterObtainController<RegisterContext.Obtain.Request, ResultContext> {

    /**
     * 注册验证码获取的服务对象
     */
    private final RegisterObtainService service;

    /**
     * 构造方法初始化
     *
     * @param service 注册验证码获取的服务对象
     */
    public RegisterObtainControllerImpl(RegisterObtainService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, RegisterContext.Obtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, RegisterContext.Obtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}
