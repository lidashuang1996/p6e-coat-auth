package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.context.QuickResponseCodeContext;
import reactor.core.publisher.Mono;

/**
 * 二维码登录服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QuickResponseCodeLoginService {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 执行二维码登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    public Mono<AuthUserDetails> execute(AuthVoucherContext voucher, QuickResponseCodeContext.Request param);

}
