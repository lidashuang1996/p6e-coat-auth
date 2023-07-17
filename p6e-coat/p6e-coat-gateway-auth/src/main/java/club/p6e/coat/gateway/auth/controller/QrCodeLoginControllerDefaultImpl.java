package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.QrCodeLoginService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginControllerDefaultImpl
        implements QrCodeLoginController<LoginContext.QrCode.Request, AuthUserDetails> {

    /**
     * 二维码登录获取信息为空的返回
     */
    private static final String QR_CODE_EMPTY_RESULT = "NO_DATA";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 二维码登录服务
     */
    private final QrCodeLoginService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    二维码登录服务
     */
    public QrCodeLoginControllerDefaultImpl(Properties properties, QrCodeLoginService service) {
        this.service = service;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable();
    }

    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param)",
                                "QrCode login service not enabled exception."
                        )))
                .flatMap(p -> service.execute(exchange, p));
    }

}
