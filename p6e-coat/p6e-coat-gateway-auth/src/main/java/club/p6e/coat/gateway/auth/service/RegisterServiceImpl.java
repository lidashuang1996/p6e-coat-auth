package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import club.p6e.coat.gateway.auth.AuthUser;
import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.RegisterContext;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.reactivestreams.Publisher;
import org.springframework.transaction.ReactiveTransaction;
import org.springframework.transaction.reactive.TransactionCallback;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
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
                               AuthPasswordEncryptor encryptor) {
        this.properties = properties;
        this.encryptor = encryptor;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public Mono<RegisterContext.Dto> execute(ServerWebExchange exchange, RegisterContext.Request param) {
        final Properties.Mode mode = properties.getMode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> switch (mode) {
                    case PHONE -> executePhoneMode(param).flatMap(u -> v.del().map(b -> u));
                    case MAILBOX -> executeMailboxMode(param).flatMap(u -> v.del().map(b -> u));
                    case ACCOUNT -> executeAccountMode(param).flatMap(u -> v.del().map(b -> u));
                    case PHONE_OR_MAILBOX -> executePhoneOrMailboxMode(param).flatMap(u -> v.del().map(b -> u));
                });
    }



    /**
     * 执行手机号码登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Flux<Object> executePhoneMode(RegisterContext.Request param) {
        return transactional.execute(new TransactionCallback<Object>() {
            @Override
            public Publisher<Object> doInTransaction(ReactiveTransaction status) {

                return userRepository.creaTE

                return null;
            }
        });
        return userRepository
                .create(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> au.create(u, a)));
    }

    /**
     * 执行邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<AuthUser.Model> executeMailboxMode(RegisterContext.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> au.create(u, a)));
    }

    /**
     * 执行账号登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<AuthUser.Model> executeAccountMode(RegisterContext.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> au.create(u, a)));
    }

    /**
     * 执行手机号码/邮箱登录
     *
     * @param param 请求对象
     * @return 结果对象
     */
    protected Mono<AuthUser.Model> executePhoneOrMailboxMode(RegisterContext.Request param) {
        return userRepository
                .findByPhoneOrMailbox(param.getAccount())
                .flatMap(u -> userAuthRepository
                        .findById(u.getId())
                        .map(a -> au.create(u, a)));
    }


}
