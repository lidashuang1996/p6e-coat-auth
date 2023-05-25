package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 二维码回调服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = QrCodeCallbackService.class,
        ignored = QrCodeCallbackServiceDefaultImpl.class
)
@ConditionalOnExpression(QrCodeCallbackService.CONDITIONAL_EXPRESSION)
public class QrCodeCallbackServiceDefaultImpl implements QrCodeCallbackService {

    /**
     * 二维码登录缓存
     */
    private final QrCodeLoginCache cache;

    /**
     * 构造方法初始化
     *
     * @param cache 二维码登录缓存
     */
    public QrCodeCallbackServiceDefaultImpl(QrCodeLoginCache cache) {
        this.cache = cache;
    }

//    @Override
//    public LoginContext.QrCodeCallback.Dto execute(LoginContext.QrCodeCallback.Request param) {
//        final String uid = param.getUid();
//        final String mark = param.getMark();
//        final Optional<String> cacheOptional = cache.get(mark);
//        if (cacheOptional.isPresent()) {
//            cache.set(mark, uid);
//            return new LoginContext.QrCodeCallback.Dto().setContent(mark);
//        } else {
//            return new LoginContext.QrCodeCallback.Dto();
//        }
//    }

    @Override
    public Mono<LoginContext.QrCodeCallback.Dto> execute(LoginContext.QrCodeCallback.Request param) {
        return Mono.just(new LoginContext.QrCodeCallback.Dto().setContent("XXXXXXXXXXXXXXX"));
    }
}
