package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.context.QuickResponseCodeContext;
import reactor.core.publisher.Mono;

/**
 * 二维码获取服务
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QuickResponseCodeObtainService {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 执行二维码获取操作
     *
     * @param param 请求对象
     * @return 结果对象
     */
    public Mono<QuickResponseCodeContext.Obtain.Dto> execute(QuickResponseCodeContext.Obtain.Request param);

}
