package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.QuickResponseCodeLoginCache;
import club.p6e.coat.gateway.auth.context.QuickResponseCodeContext;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 二维码登录服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class QuickResponseCodeLoginServiceImpl implements QuickResponseCodeLoginService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 二维码缓存对象
     */
    private final QuickResponseCodeLoginCache cache;

    /**
     * 用户存储库
     */
    private ReactiveUserDetailsService service;

    /**
     * 构造方法初始化
     *
     * @param cache      二维码缓存对象
     * @param properties 配置文件对象
     */
    public QuickResponseCodeLoginServiceImpl(
            Properties properties,
            QuickResponseCodeLoginCache cache,
            ReactiveUserDetailsService service) {
        this.cache = cache;
        this.properties = properties;
        this.service = service;
    }

    @Override
    public Mono<AuthUserDetails> execute(QuickResponseCodeContext.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getQrCode().isEnable()) {
            throw new ServiceNotEnabledException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", "");
        }
        final AuthVoucherContext avc = param.getVoucher();
        final String qcm = avc.get(AuthVoucherContext.QUICK_RESPONSE_CODE_LOGIN_MARK);
        return cache
                .get(qcm)
                .flatMap(c -> {
                    if (QuickResponseCodeLoginCache.isEmpty(c)) {
                        return Mono.error(new ServiceNotEnabledException(
                                this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", ""));
                    } else {
                        return cache
                                .del(qcm)
                                .flatMap(p -> service.findByUsername(c))
                                .map(u -> (AuthUserDetails) u);
                    }
                });
    }
}
