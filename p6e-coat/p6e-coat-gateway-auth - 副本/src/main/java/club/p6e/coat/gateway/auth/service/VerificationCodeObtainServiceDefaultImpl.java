package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.cache.VerificationCodeLoginCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.CodeLoginGenerator;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.VerificationCodeLoginContext;
import club.p6e.coat.gateway.auth.launcher.Launcher;
import club.p6e.coat.gateway.auth.launcher.LauncherType;
import club.p6e.coat.gateway.auth.utils.VerificationUtil;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 验证码获取服务的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class VerificationCodeObtainServiceDefaultImpl implements VerificationCodeObtainService {

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
    private final ReactiveUserDetailsService service;

    /**
     * 验证码生产对象
     */
    private final CodeLoginGenerator generator;

    private final VerificationCodeLoginCache cache;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public VerificationCodeObtainServiceDefaultImpl(
            Properties properties,
            VerificationCodeLoginCache cache,
            CodeLoginGenerator generator,
            ReactiveUserDetailsService service
    ) {
        this.cache = cache;
        this.properties = properties;
        this.service = service;
        this.generator = generator;
    }

    @Override
    public Mono<VerificationCodeLoginContext.Obtain.Dto> execute(
            AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getVerificationCode().isEnable()) {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun execute(AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param)",
                    "Verification code obtain service not enabled exception."
            ));
        }
        String code;
        LauncherType type;
        final String account = param.getAccount();
        if (VerificationUtil.phone(account)) {
            type = LauncherType.SMS;
            code = generator.execute(LauncherType.SMS.name());
        } else if (VerificationUtil.mailbox(account)) {
            type = LauncherType.EMAIL;
            code = generator.execute(LauncherType.EMAIL.name());
        } else {
            return Mono.error(GlobalExceptionContext.exceptionDataFormatException(
                    this.getClass(),
                    "fun execute(AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param)",
                    "Account format error."
            ));
        }
        final Map<String, String> map = new HashMap<>(4);
        map.put(AuthVoucherContext.ACCOUNT, account);
        map.put(AuthVoucherContext.ACCOUNT_TYPE, type.name());
        map.put(AuthVoucherContext.VERIFICATION_CODE_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
        return service
                .findByUsername(account)
                .switchIfEmpty(Mono.error(GlobalExceptionContext
                        .exceptionAccountException(
                                this.getClass(),
                                "fun execute(AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param)",
                                "Account does not exist exception."
                        )))
                .flatMap(c -> cache.set(account, type.name(), code))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                        this.getClass(),
                        "fun execute(AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param)",
                        "Verification code login cache write [cache.set()] exception."
                )))
                .flatMap(b -> {
                    final Map<String, String> tm = new HashMap<>(1);
                    tm.put("code", code);
                    return Launcher.push(type, account, CODE_LOGIN_TEMPLATE, tm);
                })
                .flatMap(s -> {
                    map.put(AuthVoucherContext.VERIFICATION_CODE_LOGIN_MARK, s);
                    return voucher
                            .set(map)
                            .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                                    this.getClass(),
                                    "fun execute(AuthVoucherContext voucher, VerificationCodeLoginContext.Obtain.Request param)",
                                    "Voucher write cache [cache.set()] exception."
                            )))
                            .map(c -> new VerificationCodeLoginContext.Obtain.Dto().setContent(s));
                });
    }
}
