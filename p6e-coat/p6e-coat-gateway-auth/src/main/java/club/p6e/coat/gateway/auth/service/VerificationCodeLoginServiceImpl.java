package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.VerificationCodeLoginCache;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 验证码登录服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class VerificationCodeLoginServiceImpl implements VerificationCodeLoginService {

    /**
     * 验证码缓存对象
     */
    private final VerificationCodeLoginCache cache;

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 用户存储库
     */
    private final ReactiveUserDetailsService service;

    /**
     * 构造方法初始化
     *
     * @param cache      验证码缓存对象
     * @param properties 配置文件对象
     * @param service    用户存储库
     */
    public VerificationCodeLoginServiceImpl(
            VerificationCodeLoginCache cache,
            Properties properties,
            ReactiveUserDetailsService service) {
        this.cache = cache;
        this.properties = properties;
        this.service = service;
    }

    @Override
    public Mono<AuthUserDetails> execute(VerificationCodeLoginContext.Request param) {
        // 读取配置文件判断服务是否启动
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getVerificationCode().isEnable()) {
            throw new ServiceNotEnabledException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", "");
        }
        final AuthVoucherContext avc = param.getVoucher();
        final String account = avc.get(AuthVoucherContext.ACCOUNT);
        final String accountType = avc.get(AuthVoucherContext.ACCOUNT_TYPE);
        if (account == null || accountType == null) {
            throw new RuntimeException();
        } else {
            final String code = param.getCode();
            return cache
                    .get(account, accountType)
                    .filter(l -> l.contains(code))
                    .publishOn(Schedulers.boundedElastic())
                    .doFinally(signalType -> cache.del(account, accountType, code).block())
                    .flatMap(l -> service.findByUsername(account))
                    .map(u -> (AuthUserDetails) u);
        }
    }
}
