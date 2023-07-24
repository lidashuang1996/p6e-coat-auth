package club.p6e.auth.service;

import club.p6e.auth.cache.VerificationCodeLoginCache;
import club.p6e.auth.repository.UserRepository;
import club.p6e.auth.AuthUser;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.Properties;
import club.p6e.auth.context.LoginContext;
import club.p6e.auth.error.GlobalExceptionContext;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 验证码登录服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeLoginServiceImpl implements VerificationCodeLoginService {

    /**
     * 验证码缓存对象
     */
    private final VerificationCodeLoginCache cache;

    /**
     * 用户存储库
     */
    private final UserRepository repository;

    private final AuthUser<?> au;

    /**
     * 构造方法初始化
     *
     * @param cache      验证码缓存对象
     * @param properties 配置文件对象
     * @param repository 用户存储库
     */
    public VerificationCodeLoginServiceImpl(
            AuthUser<?> au,
            VerificationCodeLoginCache cache,
            UserRepository repository) {
        this.au = au;
        this.cache = cache;
        this.repository = repository;
    }

    @Override
    public Mono<AuthUser.Model> execute(ServerWebExchange exchange, LoginContext.VerificationCode.Request param) {
        final String code = param.getCode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String account = v.get(AuthVoucher.ACCOUNT);
                    final String accountType = v.get(AuthVoucher.ACCOUNT_TYPE);
                    return cache
                            .get(accountType, account)
                            .switchIfEmpty(Mono.error(
                                    GlobalExceptionContext.executeCacheException(
                                            this.getClass(),
                                            "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                                            "Verification code login cache data does not exist or expire exception."
                                    )))
                            .flatMap(list -> {
                                System.out.println(list);
                                if (list != null && list.size() > 0) {
                                    final int index = list.indexOf(code);
                                    if (index >= 0) {
                                        return cache.del(accountType, account, code);
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
                                case PHONE -> repository.findByPhone(account);
                                case MAILBOX -> repository.findByMailbox(account);
                                case ACCOUNT -> repository.findByAccount(account);
                                case PHONE_OR_MAILBOX -> repository.findByPhoneOrMailbox(account);
                            })
                            .map(m -> au.create(m, null));
                });

    }
}
