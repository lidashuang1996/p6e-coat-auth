package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.QrCodeCallbackService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录回调实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = QrCodeCallbackController.class,
//        ignored = QrCodeCallbackControllerDefaultImpl.class
//)
//@ConditionalOnExpression(QrCodeCallbackController.CONDITIONAL_EXPRESSION)
public class QrCodeCallbackControllerDefaultImpl
        implements QrCodeCallbackController<LoginContext.QrCodeCallback.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 二维码登录回调服务
     */
    private final QrCodeCallbackService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    二维码登录的服务对象
     */
    public QrCodeCallbackControllerDefaultImpl(Properties properties, QrCodeCallbackService service) {
        this.service = service;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable();
    }

    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param)",
                                "QrCode callback service not enabled exception."
                        )))
                .flatMap(service::execute)
                .map(ResultContext::build);
    }
}
