package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.service.QrCodeObtainService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;

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
     * @param properties 配置文件对象
     * @param service    二维码获取的服务对象
     */
    public QrCodeObtainControllerImpl(Properties properties, QrCodeObtainService service) {
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