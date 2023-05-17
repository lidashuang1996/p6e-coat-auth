package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthAccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;

/**
 * 账号密码登录的密码签名服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AccountPasswordLoginSignatureServiceImpl implements AccountPasswordLoginSignatureService {

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
    public AccountPasswordLoginSignatureServiceImpl(
            Properties properties,
            AccountPasswordLoginSignatureCache cache,
            AuthAccountPasswordLoginTransmissionCodec codec) {
        this.codec = codec;
        this.properties = properties;
        this.cache = cache;
    }

    @Override
    public Mono<AccountPasswordLoginContext.Signature.Dto> execute(AuthVoucherContext voucher,AccountPasswordLoginContext.Signature.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getAccountPassword().isEnable()
                || !properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            throw new ServiceNotEnabledException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", "");
        }
        final AuthAccountPasswordLoginTransmissionCodec.Model cm = codec.generate();
        final String cv = JsonUtil.toJson(cm);
        final String ck = GeneratorUtil.uuid() + GeneratorUtil.random();
        final Map<String, String> map = new HashMap<>(2);
        map.put(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_MARK, ck);
        map.put(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_DATE, String.valueOf(System.currentTimeMillis()));
        return cache
                .set(ck, cv)
                .flatMap(b -> voucher.set(map))
                .map(m -> new AccountPasswordLoginContext.Signature.Dto().setKey(cm.getPublicKey()));
    }
}
