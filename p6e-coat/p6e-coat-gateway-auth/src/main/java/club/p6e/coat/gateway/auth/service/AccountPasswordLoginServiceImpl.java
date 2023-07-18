package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.codec.AccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 账号密码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginServiceImpl implements AccountPasswordLoginService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 密码加密器
     */
    private final AuthPasswordEncryptor encryptor;

    /**
     * 用户存储库
     */
    private final UserRepository userRepository;

    private final UserAuthRepository userAuthRepository;

    /**
     * 构造方法初始化
     *
     * @param encryptor  密码加密器
     * @param properties 配置文件对象
     */
    public AccountPasswordLoginServiceImpl(
            Properties properties,
            UserRepository userRepository,
            UserAuthRepository userAuthRepository,
            AuthPasswordEncryptor encryptor) {
        this.encryptor = encryptor;
        this.properties = properties;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
    }

    protected Mono<String> executeTransmissionDecryption(AuthVoucher voucher, String content) {
        final String mark = voucher.get(AuthVoucher.ACCOUNT_PASSWORD_CODEC_MARK);
        return Mono.just(mark)
                .flatMap(m -> {
                    if (SpringUtil.exist(AccountPasswordLoginSignatureCache.class)) {
                        return SpringUtil.getBean(AccountPasswordLoginSignatureCache.class).get(m);
                    } else {
                        return Mono.error(GlobalExceptionContext.executeCacheException(
                                this.getClass(),
                                "fun executeTransmissionDecryption(AuthVoucher voucher, String content)",
                                "Account password login transmission decryption cache handle bean not exist exception."
                        ));
                    }
                })
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                        this.getClass(),
                        "fun executeTransmissionDecryption(AuthVoucher voucher, String content)",
                        "Account password login transmission decryption cache data does not exist or expire exception."
                )))
                .flatMap(s -> {
                    try {
                        final AccountPasswordLoginTransmissionCodec codec
                                = SpringUtil.getBean(AccountPasswordLoginTransmissionCodec.class);
                        final AccountPasswordLoginTransmissionCodec.Model model
                                = JsonUtil.fromJson(s, AccountPasswordLoginTransmissionCodec.Model.class);
                        return Mono.just(codec.decryption(model, content));
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.executeCacheException(
                                this.getClass(),
                                "fun executeTransmissionDecryption(AuthVoucher voucher, String content)",
                                "Account password login transmission decryption cache data does not exist or expire exception."
                        ));
                    }
                })
                .publishOn(Schedulers.boundedElastic())
                .doFinally(signalType -> SpringUtil.getBean(AccountPasswordLoginSignatureCache.class).del(mark).block());
    }

    @Override
    public Mono<AuthUser> execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        final Properties.Mode mode = properties.getMode();
        final boolean ete = properties.getLogin().getAccountPassword().isEnableTransmissionEncryption();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> ete ? executeTransmissionDecryption(v, param.getPassword()).map(param::setPassword) : Mono.just(param))
                .flatMap(p -> switch (mode) {
                    case PHONE -> executePhoneMode(p);
                    case MAILBOX -> executeMailboxMode(p);
                    case ACCOUNT -> executeAccountMode(p);
                    case PHONE_OR_MAILBOX -> executePhoneOrMailboxMode(p);
                })
                .filter(u -> u.password().equals(encryptor.execute(param.getPassword())))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAccountPasswordLoginAccountOrPasswordException(
                        this.getClass(),
                        "fun execute(LoginContext.AccountPassword.Request param)",
                        "Account password inside login account or password exception."
                )));
    }

    /**
     * 执行手机号码登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<AuthUser> executePhoneMode(LoginContext.AccountPassword.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

    /**
     * 执行邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<AuthUser> executeMailboxMode(LoginContext.AccountPassword.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

    /**
     * 执行账号登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<AuthUser> executeAccountMode(LoginContext.AccountPassword.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

    /**
     * 执行手机号码/邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<AuthUser> executePhoneOrMailboxMode(LoginContext.AccountPassword.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

}
