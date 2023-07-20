package club.p6e.auth.service;

import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.repository.UserAuthRepository;
import club.p6e.auth.repository.UserRepository;
import club.p6e.auth.utils.VerificationUtil;
import club.p6e.auth.AuthPasswordEncryptor;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.Properties;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.model.UserAuthModel;
import club.p6e.auth.model.UserModel;
import org.reactivestreams.Publisher;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterServiceImpl implements RegisterService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 密码加密器
     */
    private final AuthPasswordEncryptor encryptor;

    /**
     * 模板对象
     */
    private final TransactionalOperator transactional;

    /**
     * 用户存储库
     */
    private final UserRepository userRepository;

    private final UserAuthRepository userAuthRepository;

    public RegisterServiceImpl(Properties properties,
                               UserRepository userRepository,
                               UserAuthRepository userAuthRepository,
                               TransactionalOperator transactional,
                               AuthPasswordEncryptor encryptor) {
        this.properties = properties;
        this.encryptor = encryptor;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
        this.transactional = transactional;
    }

    @Override
    public Mono<RegisterContext.Dto> execute(ServerWebExchange exchange, RegisterContext.Request param) {
        final Properties.Mode mode = properties.getMode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> switch (mode) {
                    case PHONE -> executePhoneMode(param).flatMap(l -> v.del())
                            .flatMap(l -> v.del()).map(b -> new RegisterContext.Dto().setAccount(param.getAccount()));
                    case MAILBOX -> executeMailboxMode(param).flatMap(l -> v.del())
                            .flatMap(l -> v.del()).map(b -> new RegisterContext.Dto().setAccount(param.getAccount()));
                    case ACCOUNT -> executeAccountMode(param).flatMap(l -> v.del())
                            .flatMap(l -> v.del()).map(b -> new RegisterContext.Dto().setAccount(param.getAccount()));
                    case PHONE_OR_MAILBOX -> executePhoneOrMailboxMode(param)
                            .flatMap(l -> v.del()).map(b -> new RegisterContext.Dto().setAccount(param.getAccount()));
                });
    }


    /**
     * 执行手机号码登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<Long> executePhoneMode(RegisterContext.Request param) {
        return transactional.execute(new TransactionCallback<Integer>() {
            @Override
            public  Publisher<Integer> doInTransaction( ReactiveTransaction status) {
                return userRepository
                        .createAccount(new UserModel().setPhone(param.getAccount()))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account exception."
                        )))
                        .flatMap(m -> userAuthRepository.create(new UserAuthModel()
                                .setPhone(param.getAccount())
                                .setPassword(param.getPassword())
                        ))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account auth exception."
                        )))
                        .map(m -> 1);
            }
        }).collectList().map(l -> (long) l.stream().reduce(0, Integer::sum));
    }

    /**
     * 执行邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<Long> executeMailboxMode(RegisterContext.Request param) {
        return transactional.execute(new TransactionCallback<Integer>() {
            @Override
            public  Publisher<Integer> doInTransaction( ReactiveTransaction status) {
                return userRepository
                        .createAccount(new UserModel().setMailbox(param.getAccount()))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account exception."
                        )))
                        .flatMap(m -> userAuthRepository.create(new UserAuthModel()
                                .setMailbox(param.getAccount())
                                .setPassword(param.getPassword())
                        ))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account auth exception."
                        )))
                        .map(m -> 1);
            }
        }).collectList().map(l -> (long) l.stream().reduce(0, Integer::sum));
    }

    /**
     * 执行账号登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<Long> executeAccountMode(RegisterContext.Request param) {
        return transactional.execute(new TransactionCallback<Integer>() {
            @Override
            public  Publisher<Integer> doInTransaction( ReactiveTransaction status) {
                return userRepository
                        .createAccount(new UserModel().setAccount(param.getAccount()))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account exception."
                        )))
                        .flatMap(m -> userAuthRepository.create(new UserAuthModel()
                                .setAccount(param.getAccount())
                                .setPassword(param.getPassword())
                        ))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account auth exception."
                        )))
                        .map(m -> 1);
            }
        }).collectList().map(l -> (long) l.stream().reduce(0, Integer::sum));
    }

    /**
     * 执行手机号码/邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<Long> executePhoneOrMailboxMode(RegisterContext.Request param) {
        return transactional.execute(new TransactionCallback<Integer>() {
            @Override
            public  Publisher<Integer> doInTransaction( ReactiveTransaction status) {
                return userRepository
                        .createAccount(new UserModel()
                                .setPhone(VerificationUtil.phone(param.getAccount()) ? param.getAccount() : null)
                                .setMailbox(VerificationUtil.mailbox(param.getAccount()) ? param.getAccount() : null)
                        )
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account exception."
                        )))
                        .flatMap(m -> userAuthRepository.create(new UserAuthModel()
                                .setPassword(param.getPassword())
                                .setPhone(VerificationUtil.phone(param.getAccount()) ? param.getAccount() : null)
                                .setMailbox(VerificationUtil.mailbox(param.getAccount()) ? param.getAccount() : null)
                        ))
                        .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionDataBaseException(
                                this.getClass(),
                                "fun executePhoneMode(RegisterContext.Request param).",
                                "[ TransactionCallback ] create user phone account auth exception."
                        )))
                        .map(m -> 1);
            }
        }).collectList().map(l -> (long) l.stream().reduce(0, Integer::sum));
    }


}
