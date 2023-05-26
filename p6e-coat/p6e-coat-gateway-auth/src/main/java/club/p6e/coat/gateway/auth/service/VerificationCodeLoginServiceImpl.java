package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.VerificationCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码登录服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = VerificationCodeLoginService.class,
//        ignored = VerificationCodeLoginServiceImpl.class
//)
//@ConditionalOnExpression(VerificationCodeLoginService.CONDITIONAL_EXPRESSION)
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
    private final UserRepository repository;

    /**
     * 构造方法初始化
     *
     * @param cache      验证码缓存对象
     * @param properties 配置文件对象
     * @param repository 用户存储库
     */
    public VerificationCodeLoginServiceImpl(
            VerificationCodeLoginCache cache,
            Properties properties,
            UserRepository repository) {
        this.cache = cache;
        this.properties = properties;
        this.repository = repository;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable();
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.VerificationCode.Request param) {
        if (!isEnable()) {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                    "Verification code login code obtain service not enabled exception."
            ));
        }
        final String code = param.getCode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String account = v.get(AuthVoucher.ACCOUNT);
                    final String accountType = v.get(AuthVoucher.ACCOUNT_TYPE);
                    return cache
                            .get(account, accountType)
                            .switchIfEmpty(Mono.error(
                                    GlobalExceptionContext.executeCacheException(
                                            this.getClass(),
                                            "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                                            "Verification code login cache data does not exist or expire exception."
                                    )))
                            .flatMap(list -> {
                                if (list != null && list.size() > 0) {
                                    final int index = list.indexOf(code);
                                    if (index >= 0) {
                                        return cache.del(account, accountType, code);
                                    }
                                }
                                return Mono.error(
                                        GlobalExceptionContext.executeCacheException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                                                "Verification code login cache data does not exist or expire exception."
                                        ));
                            })
                            .flatMap(r -> switch (Properties.Mode.create(accountType)) {
                                case PHONE -> repository.findOneByPhone(account);
                                case MAILBOX -> repository.findOneByMailbox(account);
                                case ACCOUNT -> repository.findOneByAccount(account);
                                case PHONE_OR_MAILBOX -> repository.findOneByPhoneOrMailbox(account);
                            })
                            .map(AuthUserDetails::new);
                });

    }
}
