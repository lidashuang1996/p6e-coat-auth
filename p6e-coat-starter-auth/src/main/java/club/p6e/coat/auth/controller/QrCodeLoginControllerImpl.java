package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthCertificateAuthority;
import club.p6e.coat.auth.context.LoginContext;
import club.p6e.coat.auth.context.ResultContext;
import club.p6e.coat.auth.service.QrCodeLoginService;
import club.p6e.coat.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 二维码登录实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginControllerImpl
        implements QrCodeLoginController<LoginContext.QrCode.Request, ResultContext> {

    /**
     * 二维码登录服务
     */
    private final QrCodeLoginService service;

    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 构造方法初始化
     *
     * @param service   二维码登录服务
     * @param authority 认证授权的服务对象
     */
    public QrCodeLoginControllerImpl(QrCodeLoginService service, AuthCertificateAuthority authority) {
        this.service = service;
        this.authority = authority;
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .flatMap(u -> authority.award(exchange, u));
    }

}
