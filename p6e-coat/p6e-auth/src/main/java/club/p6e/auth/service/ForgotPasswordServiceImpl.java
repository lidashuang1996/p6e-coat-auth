package club.p6e.auth.service;

import club.p6e.auth.AuthPasswordEncryptor;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.Properties;
import club.p6e.auth.cache.ForgotPasswordCodeCache;
import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.repository.UserAuthRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserAuthRepository userAuthRepository;

    private final AuthPasswordEncryptor passwordEncryptor;

    private final ForgotPasswordCodeCache cache;

    public ForgotPasswordServiceImpl(Properties properties, UserAuthRepository userAuthRepository, AuthPasswordEncryptor passwordEncryptor, ForgotPasswordCodeCache cache) {
        this.properties = properties;
        this.userAuthRepository = userAuthRepository;
        this.passwordEncryptor = passwordEncryptor;
        this.cache = cache;
    }

    @Override
    public Mono<ForgotPasswordContext.Dto> execute(ServerWebExchange exchange, ForgotPasswordContext.Request param) {
        final Properties.Mode mode = properties.getMode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> cache
                        .get(v.getAccount())
                        .filter(l -> l.contains(param.getCode()))
                        .flatMap(l -> {
                            param.setAccount(v.getAccount());
                            return cache.del(v.getAccount()).map(ll -> v);
                        }))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeQrCodeDataNullException(
                        this.getClass(),
                        "",
                        ""
                )))
                .flatMap(v -> switch (mode) {
                    case PHONE -> userAuthRepository.findByPhone(param.getAccount());
                    case MAILBOX -> userAuthRepository.findByMailbox(param.getAccount());
                    case ACCOUNT -> userAuthRepository.findByAccount(param.getAccount());
                    case PHONE_OR_MAILBOX -> userAuthRepository.findByPhoneOrMailbox(param.getAccount());
                })
                .flatMap(m -> userAuthRepository.updatePassword(m.getId(), passwordEncryptor.execute(param.getPassword())))
                .map(l -> new ForgotPasswordContext.Dto().setAccount(param.getAccount()));
    }

}
