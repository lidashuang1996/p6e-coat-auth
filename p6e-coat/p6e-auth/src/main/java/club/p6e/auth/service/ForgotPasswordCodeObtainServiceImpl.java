package club.p6e.auth.service;

import club.p6e.auth.AuthVoucher;
import club.p6e.auth.Properties;
import club.p6e.auth.cache.ForgotPasswordCodeCache;
import club.p6e.auth.context.ForgotPasswordContext;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.generator.ForgotPasswordCodeGenerator;
import club.p6e.auth.launcher.Launcher;
import club.p6e.auth.launcher.LauncherType;
import club.p6e.auth.repository.UserRepository;
import club.p6e.auth.utils.VerificationUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordCodeObtainServiceImpl implements ForgotPasswordCodeObtainService {

    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserRepository repository;


    private final ForgotPasswordCodeCache cache;

    private final ForgotPasswordCodeGenerator generator;

    private final String TEMPLATE = "FP_TEMPLATE";

    public ForgotPasswordCodeObtainServiceImpl(Properties properties, UserRepository repository, ForgotPasswordCodeCache cache, ForgotPasswordCodeGenerator generator) {
        this.properties = properties;
        this.repository = repository;
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public Mono<ForgotPasswordContext.Dto> execute(ServerWebExchange exchange, ForgotPasswordContext.Request param) {
        final Properties.Mode mode = properties.getMode();
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> (switch (mode) {
                            case PHONE -> repository.findByPhone(param.getAccount());
                            case MAILBOX -> repository.findByMailbox(param.getAccount());
                            case ACCOUNT -> repository.findByAccount(param.getAccount());
                            case PHONE_OR_MAILBOX -> repository.findByPhoneOrMailbox(param.getAccount());
                        })
                                .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionAccountException(
                                        this.getClass(),
                                        "",
                                        ""
                                )))
                                .flatMap(m -> {
                                    final String code = generator.execute();
                                    final String account = param.getAccount();
                                    final LauncherType type = VerificationUtil.phone(account) ? LauncherType.SMS : LauncherType.EMAIL;
                                    final Map<String, String> data = new HashMap<>();
                                    data.put("code", code);
                                    return Launcher
                                            .push(type, account, TEMPLATE, data)
                                            .flatMap(l -> cache.set(account, code));
                                })
                )
                .map(b -> new ForgotPasswordContext.Dto().setAccount(param.getAccount()));
    }

}
