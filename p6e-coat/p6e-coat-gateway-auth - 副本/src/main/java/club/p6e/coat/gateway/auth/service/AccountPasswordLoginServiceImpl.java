package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.*;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.certificate.AuthCertificate;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AccountPasswordLoginServiceImpl implements AccountPasswordLoginService {


    /**
     * 配置文件对象
     */
    private final Properties properties;
    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService service;
    private final AccountPasswordLoginSignatureCache cache;
    private final AuthAccountPasswordLoginTransmissionCodec codec;

    /**
     * 构造方法初始化
     *
     * @param codec      传输编码解码器
     * @param properties 配置文件对象
     */
    public AccountPasswordLoginServiceImpl(
            Properties properties,
            PasswordEncoder passwordEncoder,
            ReactiveUserDetailsService service,
            AccountPasswordLoginSignatureCache cache,
            AuthAccountPasswordLoginTransmissionCodec codec) {
        this.cache = cache;
        this.codec = codec;
        this.service = service;
        this.properties = properties;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Mono<AuthUserDetails> execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getAccountPassword().isEnable()) {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param)",
                    "Account password login service not enabled exception."
            ));
        }
        Mono<AccountPasswordLoginContext.Request> mono = Mono.just(param);
        if (properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            final String mark = voucher.get(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_MARK);
            mono = Mono
                    .just(mark)
                    .flatMap(cache::get)
                    .switchIfEmpty(Mono.error(GlobalExceptionContext
                            .exceptionAccountPasswordLoginTransmissionException(
                                    this.getClass(),
                                    "fun execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param)",
                                    "Password transmission cache data [cache.get()] exception."
                            )))
                    .flatMap(c -> cache.del(c).map(b -> c))
                    .flatMap(c -> {
                        final AuthAccountPasswordLoginTransmissionCodec.Model cm =
                                JsonUtil.fromJson(c, AuthAccountPasswordLoginTransmissionCodec.Model.class);
                        try {
                            param.setPassword(codec.decrypt(cm, param.getPassword()));
                            return Mono.just(param);
                        } catch (Exception e) {
                            return Mono.error(GlobalExceptionContext
                                    .exceptionAccountPasswordLoginTransmissionException(
                                            this.getClass(),
                                            "fun execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param)",
                                            "Password decrypt [codec.decrypt()] exception."
                                    ));
                        }
                    });
        }
        return mono
                .flatMap(p -> service.findByUsername(param.getAccount()))
                .filter(u -> u.getPassword().equals(passwordEncoder.encode(param.getPassword())))
                .map(AuthUserDetails::new)
                .switchIfEmpty(Mono.error(GlobalExceptionContext
                        .exceptionAccountPasswordLoginAccountOrPasswordException(
                                this.getClass(),
                                "fun execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param)",
                                "Account password login account or password exception."
                        )));
    }

}
