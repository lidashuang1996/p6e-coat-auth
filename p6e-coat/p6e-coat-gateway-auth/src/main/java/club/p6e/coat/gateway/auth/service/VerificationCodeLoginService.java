package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.context.LoginContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码登录服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VerificationCodeLoginService {

    /**
     * 执行验证码登录操作
     *
     * @param param 请求对象
     * @return 结果对象
     */
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.VerificationCode.Request param);

}
