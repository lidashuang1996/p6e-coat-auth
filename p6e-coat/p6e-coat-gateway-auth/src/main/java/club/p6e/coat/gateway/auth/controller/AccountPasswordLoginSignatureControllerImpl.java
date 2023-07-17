package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.AccountPasswordLoginSignatureService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的密码签名的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginSignatureControllerImpl implements
        AccountPasswordLoginSignatureController<LoginContext.AccountPasswordSignature.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 账号密码登录的密码签名的服务对象
     */
    private final AccountPasswordLoginSignatureService service;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param service    账号密码登录的密码签名的服务对象
     */
    public AccountPasswordLoginSignatureControllerImpl(
            Properties properties,
            AccountPasswordLoginSignatureService service) {
        this.service = service;
        this.properties = properties;
    }

    /**
     * 判断是否启用
     *
     * @return 是否启用
     */
    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()
                && properties.getLogin().getAccountPassword().isEnableTransmissionEncryption();
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param)",
                                "Account password login signature service not enabled exception."
                        )))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}
