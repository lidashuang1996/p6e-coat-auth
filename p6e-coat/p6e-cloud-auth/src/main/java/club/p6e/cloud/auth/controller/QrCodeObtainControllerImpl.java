package club.p6e.cloud.auth.controller;

import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.context.ResultContext;
import club.p6e.cloud.auth.service.QrCodeObtainService;
import club.p6e.cloud.auth.validator.ParameterValidator;

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