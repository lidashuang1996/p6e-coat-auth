package club.p6e.auth.service;

import club.p6e.auth.AuthVoucher;
import club.p6e.auth.Properties;
import club.p6e.auth.cache.RegisterCodeCache;
import club.p6e.auth.context.RegisterContext;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.generator.RegisterCodeGenerator;
import club.p6e.auth.launcher.Launcher;
import club.p6e.auth.launcher.LauncherType;
import club.p6e.auth.model.UserAuthModel;
import club.p6e.auth.model.UserModel;
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
public class RegisterObtainServiceImpl implements RegisterObtainService {

    /**
     * 配置文件对象
     */
    private final Properties properties;
    /**
     * CODE LOGIN 模板
     */
    private static final String REGISTER_TEMPLATE = "REGISTER_TEMPLATE";

    private final RegisterCodeCache cache;
    private final RegisterCodeGenerator generator;

    /**
     * 用户存储库
     */
    private final UserRepository userRepository;

    public RegisterObtainServiceImpl(RegisterCodeCache cache, UserRepository userRepository, RegisterCodeGenerator generator, Properties properties) {
        this.cache = cache;
        this.generator = generator;
        this.properties = properties;
        this.userRepository = userRepository;
    }

    @Override
    public Mono<RegisterContext.Obtain.Dto> execute(ServerWebExchange exchange, RegisterContext.Obtain.Request param) {
        final String account = param.getAccount();
        final Properties.Mode mode = properties.getMode();
        final boolean pb = VerificationUtil.phone(param.getAccount());
        final boolean mb = VerificationUtil.mailbox(param.getAccount());
        final LauncherType type = pb ? LauncherType.SMS : mb ? LauncherType.EMAIL : null;
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> (switch (mode) {
                            case PHONE -> userRepository.findByPhone(param.getAccount());
                            case MAILBOX -> userRepository.findByMailbox(param.getAccount());
                            case ACCOUNT -> userRepository.findByAccount(param.getAccount());
                            case PHONE_OR_MAILBOX -> userRepository.findByPhoneOrMailbox(param.getAccount());
                        })
                        .switchIfEmpty(Mono.just(new UserModel().setId(-1)))
                        .flatMap(m -> {
                            if (m != null && m.getId() != null && m.getId() > 0) {
                                return Mono.error(GlobalExceptionContext.exceptionAccountException(
                                        this.getClass(),
                                        "",
                                        ""
                                ));
                            } else {
                                return Mono.just(v);
                            }
                        }))
                .flatMap(v -> v.setAccount(account))
                .flatMap(v -> {
                    final String code = generator.execute();
                    return cache
                            .set(account, code)
                            .flatMap(b -> {
                                final Map<String, String> map = new HashMap<>();
                                map.put("code", code);
                                return Launcher.push(type, account, REGISTER_TEMPLATE, map);
                            });
                })
                .map(m -> new RegisterContext.Obtain.Dto().setAccount(account).setMessage(m));
    }

}
