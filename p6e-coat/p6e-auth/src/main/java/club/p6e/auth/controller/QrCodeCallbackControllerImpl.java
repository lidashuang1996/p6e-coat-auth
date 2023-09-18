package club.p6e.auth.controller;

import club.p6e.auth.context.LoginContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.QrCodeCallbackService;
import club.p6e.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录回调实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeCallbackControllerImpl
        implements QrCodeCallbackController<LoginContext.QrCodeCallback.Request, ResultContext> {

    /**
     * 二维码登录回调服务
     */
    private final QrCodeCallbackService service;

    /**
     * 构造方法初始化
     *
     * @param service 二维码登录的服务对象
     */
    public QrCodeCallbackControllerImpl(QrCodeCallbackService service) {
        this.service = service;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
        return vp(exchange, param).then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }
}
