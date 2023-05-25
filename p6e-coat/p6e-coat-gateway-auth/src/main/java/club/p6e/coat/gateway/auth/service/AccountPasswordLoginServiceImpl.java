package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    /**
     * 密码加密器
     */
    private final AuthPasswordEncryptor encryptor;

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
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable();
    }

    protected Mono<String> transmissionDecryption(String content) {
        return Mono.just("123456");
    }

    @Override
    public Mono<AuthUserDetails> execute(LoginContext.AccountPassword.Request param) {
        final Properties.Mode mode = properties.getMode();
        final boolean ete = properties.getLogin().getAccountPassword().isEnableTransmissionEncryption();
        return Mono
                .just(isEnable())
                .flatMap(b -> b ? Mono.just(ete) : Mono.error(
                        GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(LoginContext.AccountPassword.Request param)",
                                "Account password login service not enabled exception."
                        )))
                .flatMap(b -> b ? transmissionDecryption(param.getPassword()).map(param::setPassword) : Mono.just(param))
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
                .findOneByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findOneById(u.getId())
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
                .findOneByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findOneById(u.getId())
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
                .findOneByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findOneById(u.getId())
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
                .findOneByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findOneById(u.getId())
                        .map(a -> new AuthUserDetails(u, a)));
    }

}
