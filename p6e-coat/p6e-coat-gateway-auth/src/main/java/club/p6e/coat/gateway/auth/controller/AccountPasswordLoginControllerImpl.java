package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthCertificate;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.service.AccountPasswordLoginService;
import club.p6e.coat.gateway.auth.validator.ParameterValidator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = AccountPasswordLoginController.class,
//        ignored = AccountPasswordLoginControllerDefaultImpl.class
//)
//@ConditionalOnExpression(AccountPasswordLoginController.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginControllerImpl implements
        AccountPasswordLoginController<LoginContext.AccountPassword.Request, AuthUserDetails> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 账号密码登录的服务对象
     */
    private final AccountPasswordLoginService service;

    /**
     * 构造方法初始化
     *
     * @param service    账号密码登录的服务对象
     * @param properties 配置文件对象
     */
    public AccountPasswordLoginControllerImpl(Properties properties, AccountPasswordLoginService service) {
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
                && properties.getLogin().getAccountPassword().isEnable();
    }

    /**
     * 参数验证
     *
     * @param exchange ServerWebExchange 对象
     * @param param    参数对象
     * @return Mono/Void 对象
     */
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? vp(exchange, param).then(Mono.just(param)) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(service::execute);
    }

}
