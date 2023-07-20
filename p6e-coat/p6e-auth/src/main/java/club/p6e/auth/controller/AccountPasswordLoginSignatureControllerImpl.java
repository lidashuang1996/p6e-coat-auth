package club.p6e.auth.controller;

import club.p6e.auth.context.LoginContext;
import club.p6e.auth.context.ResultContext;
import club.p6e.auth.service.AccountPasswordLoginSignatureService;
import club.p6e.auth.validator.ParameterValidator;
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
     * 账号密码登录的密码签名的服务对象
     */
    private final AccountPasswordLoginSignatureService service;

    /**
     * 构造方法
     *
     * @param service    账号密码登录的密码签名的服务对象
     */
    public AccountPasswordLoginSignatureControllerImpl(AccountPasswordLoginSignatureService service) {
        this.service = service;
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
        return vp(exchange, param)
                .then(Mono.just(param))
                .flatMap(p -> service.execute(exchange, p))
                .map(ResultContext::build);
    }

}
