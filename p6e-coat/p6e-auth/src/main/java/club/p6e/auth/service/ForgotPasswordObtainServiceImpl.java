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
 * 忘记密码发送验证码服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordObtainServiceImpl implements ForgotPasswordObtainService {

    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserRepository repository;


    private final ForgotPasswordCodeCache cache;

    private final ForgotPasswordCodeGenerator generator;

    private final String TEMPLATE = "FP_TEMPLATE";

    public ForgotPasswordObtainServiceImpl(
            Properties properties,
            UserRepository repository,
            ForgotPasswordCodeCache cache,
            ForgotPasswordCodeGenerator generator) {
        this.cache = cache;
        this.generator = generator;
        this.properties = properties;
        this.repository = repository;
    }

    @Override
    public Mono<ForgotPasswordContext.Obtain.Dto> execute(
            ServerWebExchange exchange,
            ForgotPasswordContext.Obtain.Request param
    ) {
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
                                        "fun execute(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param)",
                                        "forgot password obtain code account not exist exception."
                                )))
                                .flatMap(m -> {
                                    final String code = generator.execute();
                                    final String account = param.getAccount();
                                    final LauncherType type;
                                    if (VerificationUtil.validationPhone(account)) {
                                        type = LauncherType.SMS;
                                    } else if (VerificationUtil.validationMailbox(account)) {
                                        type = LauncherType.EMAIL;
                                    } else {
                                        return Mono.error(GlobalExceptionContext.exceptionLauncherTypeException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param)",
                                                "forgot password obtain code type (LauncherType) exception."
                                        ));
                                    }
                                    final Map<String, String> data = new HashMap<>(1);
                                    data.put("code", code);
                                    return v.setAccount(account)
                                            .flatMap(a -> cache.set(account, code))
                                            .filter(b -> b)
                                            .switchIfEmpty(Mono.error(GlobalExceptionContext.exceptionCacheWritingException(
                                                    this.getClass(),
                                                    "fun execute(ServerWebExchange exchange, ForgotPasswordContext.Obtain.Request param)",
                                                    "forgot password obtain code cache writing exception."
                                            )))
                                            .flatMap(b -> Launcher.push(type, account, TEMPLATE, data));
                                })
                )
                .map(s -> new ForgotPasswordContext.Obtain.Dto().setAccount(param.getAccount()).setMessage(s));
    }
}
