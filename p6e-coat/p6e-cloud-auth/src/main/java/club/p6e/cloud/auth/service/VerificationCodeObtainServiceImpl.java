package club.p6e.cloud.auth.service;

import club.p6e.cloud.auth.cache.VerificationCodeLoginCache;
import club.p6e.cloud.auth.launcher.Launcher;
import club.p6e.cloud.auth.launcher.LauncherType;
import club.p6e.cloud.auth.repository.UserRepository;
import club.p6e.cloud.auth.utils.VerificationUtil;
import club.p6e.cloud.auth.AuthVoucher;
import club.p6e.cloud.auth.Properties;
import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import club.p6e.cloud.auth.generator.CodeLoginGenerator;
import club.p6e.cloud.auth.model.UserModel;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码获取服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeObtainServiceImpl implements VerificationCodeObtainService {

    /**
     * CODE LOGIN 模板
     */
    private static final String CODE_LOGIN_TEMPLATE = "CODE_LOGIN_TEMPLATE";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 用户存储库
     */
    private final UserRepository repository;

    /**
     * 验证码生产对象
     */
    private final CodeLoginGenerator generator;

    /**
     * 验证码缓存对象
     */
    private final VerificationCodeLoginCache cache;


    /**
     * 构造方法初始化
     *
     * @param properties         配置文件对象
     * @param repository         用户存储库对象
     * @param codeLoginCache     验证码缓存对象
     * @param codeLoginGenerator 验证码生成器对象
     */
    public VerificationCodeObtainServiceImpl(
            Properties properties, UserRepository repository,
            VerificationCodeLoginCache codeLoginCache, CodeLoginGenerator codeLoginGenerator) {
        this.cache = codeLoginCache;
        this.properties = properties;
        this.repository = repository;
        this.generator = codeLoginGenerator;
    }

    @Override
    public Mono<LoginContext.VerificationCodeObtain.Dto> execute(
            ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param) {
        Mono<UserModel> mono;
        LauncherType type = null;
        final String account = param.getAccount();
        switch (properties.getMode()) {
            case PHONE -> {
                type = LauncherType.SMS;
                mono = repository.findByPhone(account);
            }
            case MAILBOX -> {
                type = LauncherType.EMAIL;
                mono = repository.findByMailbox(account);
            }
            case PHONE_OR_MAILBOX -> {
                if (VerificationUtil.phone(account)) {
                    type = LauncherType.SMS;
                } else if (VerificationUtil.mailbox(account)) {
                    type = LauncherType.EMAIL;
                }
                mono = repository.findByPhoneOrMailbox(account);
            }
            default -> {
                return Mono.error(GlobalExceptionContext.executeServiceNotSupportException(
                        this.getClass(),
                        "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                        "Verification code obtain service not supported. [" + account + "]"
                ));
            }
        }
        if (type == null) {
            return Mono.error(GlobalExceptionContext.executeServiceNotSupportException(
                    this.getClass(),
                    "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                    "Verification code obtain <type> service not supported. [" + account + "]"
            ));
        } else {
            final LauncherType ft = type;
            final Mono<UserModel> fm = mono;
            final String code = generator.execute(type.name());
            return AuthVoucher
                    .init(exchange)
                    .flatMap(v -> fm
                            .flatMap(u -> cache.set(account, ft.name(), code))
                            .flatMap(b -> {
                                if (b) {
                                    final Map<String, String> tc = new HashMap<>(1);
                                    tc.put("code", code);
                                    return Launcher.push(ft, account, CODE_LOGIN_TEMPLATE, tc);
                                } else {
                                    return Mono.error(GlobalExceptionContext.executeCacheException(
                                            this.getClass(),
                                            "fun execute(ServerWebExchange exchange, LoginContext.VerificationCodeObtain.Request param)",
                                            "Verification code obtain write cache error."
                                    ));
                                }
                            })
                            .flatMap(m -> {
                                final Map<String, String> map = new HashMap<>(4);
                                map.put(AuthVoucher.ACCOUNT, account);
                                map.put(AuthVoucher.ACCOUNT_TYPE, ft.name());
                                map.put(AuthVoucher.VERIFICATION_CODE_LOGIN_MARK, m);
                                map.put(AuthVoucher.VERIFICATION_CODE_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
                                return v.set(map).map(vv -> m);
                            }))
                    .map(m -> new LoginContext.VerificationCodeObtain.Dto().setAccount(account).setMessage(m));
        }
    }

}

