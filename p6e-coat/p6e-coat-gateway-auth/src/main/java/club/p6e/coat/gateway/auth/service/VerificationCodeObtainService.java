package club.p6e.coat.gateway.auth.service;

import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import reactor.core.publisher.Mono;

/**
 * 验证码获取服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VerificationCodeObtainService {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.verification-code.enable:false}}";

    /**
     * 执行验证码获取
     *
     * @param param 请求对象
     * @return 结果对象
     */
    public Mono<VerificationCodeLoginContext.Obtain.Dto> execute(VerificationCodeLoginContext.Obtain.Request param);

}
