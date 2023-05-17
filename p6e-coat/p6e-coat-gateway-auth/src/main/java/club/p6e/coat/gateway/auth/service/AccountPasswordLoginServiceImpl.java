package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthAccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AccountPasswordLoginService.class,
        ignored = AccountPasswordLoginServiceImpl.class
)
public class AccountPasswordLoginServiceImpl implements AccountPasswordLoginService {

    private ReactiveUserDetailsService service;

    /**
     * 配置文件对象
     */
    private final Properties properties;
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
            ReactiveUserDetailsService service,
            AccountPasswordLoginSignatureCache cache,
            AuthAccountPasswordLoginTransmissionCodec codec) {
        this.codec = codec;
        this.service = service;
        this.properties = properties;
        this.cache = cache;
    }

    @Override
    public Mono<AuthUserDetails> execute(AuthVoucherContext voucher, AccountPasswordLoginContext.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getAccountPassword().isEnable()) {
            throw new ServiceNotEnabledException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", "");
        }
        Mono<AccountPasswordLoginContext.Request> mono = Mono.just(param);
        if (properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            final String mark = voucher.get(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_MARK);
            mono = Mono
                    .just(mark)
                    .flatMap(cache::get)
                    .map(c -> {
                        final AuthAccountPasswordLoginTransmissionCodec.Model cm =
                                JsonUtil.fromJson(c, AuthAccountPasswordLoginTransmissionCodec.Model.class);
                        param.setPassword(codec.decrypt(cm, param.getPassword()));
                        return param;
                    });
        }
        return mono
                .flatMap(p -> service.findByUsername(param.getAccount()))
                .filter(u -> u.getPassword().equals(param.getPassword()))
                .map(u -> (AuthUserDetails) u);
    }

}
