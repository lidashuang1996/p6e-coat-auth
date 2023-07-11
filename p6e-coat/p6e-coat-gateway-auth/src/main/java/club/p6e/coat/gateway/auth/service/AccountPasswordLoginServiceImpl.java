package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.codec.AuthAccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

/**
 * 账号密码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = AccountPasswordLoginService.class,
//        ignored = AccountPasswordLoginServiceImpl.class
//)
//@ConditionalOnExpression(AccountPasswordLoginService.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginServiceImpl implements AccountPasswordLoginService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserRepository userRepository;

    private final UserAuthRepository userAuthRepository;

    private final AccountPasswordLoginSignatureCache cache;
    /**
     * 密码加密器
     */
    private final AuthPasswordEncryptor encryptor;

    private final AuthAccountPasswordLoginTransmissionCodec codec;

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
            AccountPasswordLoginSignatureCache cache,
            AuthAccountPasswordLoginTransmissionCodec codec,
            AuthPasswordEncryptor encryptor) {
        this.encryptor = encryptor;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
        this.properties = properties;
        this.cache = cache;
        this.codec = codec;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable();
    }

    protected Mono<String> executeTransmissionDecryption(AuthVoucher voucher, String content) {
        final String mark = voucher.get(AuthVoucher.ACCOUNT_PASSWORD_CODEC_MARK);
        return Mono.just(mark)
                .flatMap(cache::get)
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                        this.getClass(),
                        "fun executeTransmissionDecryption(AuthVoucher voucher, String content)",
                        "Account password login transmission decryption cache data does not exist or expire exception."
                )))
                .flatMap(s -> {
                    try {
                        final AuthAccountPasswordLoginTransmissionCodec.Model model
                                = JsonUtil.fromJson(s, AuthAccountPasswordLoginTransmissionCodec.Model.class);
                        return Mono.just(codec.decrypt(model, content));
                    } catch (Exception e) {
                        return Mono.error(GlobalExceptionContext.executeCacheException(
                                this.getClass(),
                                "fun executeTransmissionDecryption(AuthVoucher voucher, String content)",
                                "Account password login transmission decryption cache data does not exist or expire exception."
                        ));
                    }
                })
                .publishOn(Schedulers.boundedElastic())
                .doFinally(signalType -> cache.del(mark).block());
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.AccountPassword.Request param) {
        final Properties.Mode mode = properties.getMode();
        final boolean ete = properties.getLogin().getAccountPassword().isEnableTransmissionEncryption();
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? AuthVoucher.init(exchange) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(v -> ete ? executeTransmissionDecryption(v, param.getPassword()).map(param::setPassword) : Mono.just(param))
                .flatMap(p -> switch (mode) {
                    case PHONE -> executePhoneMode(p);
                    case MAILBOX -> executeMailboxMode(p);
                    case ACCOUNT -> executeAccountMode(p);
                    case PHONE_OR_MAILBOX -> executePhoneOrMailboxMode(p);
                })
                .filter(u -> {
                    System.out.println(u.getPassword());
                    System.out.println(encryptor.execute(param.getPassword()));
                    return u.getPassword().equals(encryptor.execute(param.getPassword()));
                })
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
    private Mono<AuthUserDetails> executePhoneMode(LoginContext.AccountPassword.Request param) {
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
    private Mono<AuthUserDetails> executeMailboxMode(LoginContext.AccountPassword.Request param) {
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
    protected Mono<AuthUserDetails> executeAccountMode(LoginContext.AccountPassword.Request param) {
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
    protected Mono<AuthUserDetails> executePhoneOrMailboxMode(LoginContext.AccountPassword.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

}
