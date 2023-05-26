package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.context.LoginContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的密码签名服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AccountPasswordLoginSignatureService {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{"
            + "${p6e.auth.login.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable-transmission-encryption:false}"
            + "}";

    /**
     * 执行账号密码登录密码签名操作
     *
     * @param param 请求对象
     * @return 结果对象
     */
    public Mono<LoginContext.AccountPasswordSignature.Dto> execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param);

}
