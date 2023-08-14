package club.p6e.auth.controller;

import club.p6e.auth.context.LoginContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.QrCodeObtainService;
import club.p6e.auth.validator.ParameterValidator;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录获取实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeObtainControllerImpl
        implements QrCodeObtainController<LoginContext.QrCodeObtain.Request, ResultContext> {

    /**
     * 二维码获取服务
     */
    private final QrCodeObtainService service;

    /**
     * 构造方法
     *
     * @param service 二维码获取的服务对象
     */
    public QrCodeObtainControllerImpl(QrCodeObtainService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }
}