package club.p6e.auth.service;

import club.p6e.auth.context.ForgotPasswordContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 忘记密码发送验证码服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface ForgotPasswordObtainService {

    /**
     * 执行账号密码登录密码签名操作
     *
     * @param exchange ServerWebExchange 对象
     * @param param    请求对象
     * @return 结果对象
     */
    public Mono<ForgotPasswordContext.Obtain.Dto> execute(
            ServerWebExchange exchange,
            ForgotPasswordContext.Obtain.Request param
    );

}
