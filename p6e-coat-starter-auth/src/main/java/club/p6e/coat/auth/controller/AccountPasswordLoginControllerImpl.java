package club.p6e.coat.auth.controller;

import club.p6e.coat.auth.AuthCertificateAuthority;
import club.p6e.coat.auth.context.LoginContext;
import club.p6e.coat.auth.service.AccountPasswordLoginService;
import club.p6e.coat.auth.validator.ParameterValidator;
import club.p6e.coat.common.context.ResultContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginControllerImpl implements
        AccountPasswordLoginController<LoginContext.AccountPassword.Request, ResultContext> {

    /**
     * 认证授权的服务对象
     */
    private final AuthCertificateAuthority authority;

    /**
     * 账号密码登录的服务对象
     */
    private final AccountPasswordLoginService service;

    /**
     * 构造方法初始化
     *
     * @param service   账号密码登录的服务对象
     * @param authority 认证授权的服务对象
     */
    public AccountPasswordLoginControllerImpl(
            AuthCertificateAuthority authority,
            AccountPasswordLoginService service
    ) {
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
    protected Mono<Void> vp(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        return ParameterValidator.execute(exchange, param);
    }

    @Override
    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .flatMap(u -> authority.award(exchange, u));
    }

}
