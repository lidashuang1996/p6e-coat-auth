package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthAccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.AccountPasswordLoginSignatureGenerator;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;

/**
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
    private final AccountPasswordLoginSignatureGenerator generator;

    /**
     * 构造方法初始化
     *
     * @param codec      传输编码解码器
     * @param properties 配置文件对象
     */
    public AccountPasswordLoginSignatureServiceImpl(
            Properties properties,
            AccountPasswordLoginSignatureCache cache,
            AccountPasswordLoginSignatureGenerator generator,
            AuthAccountPasswordLoginTransmissionCodec codec) {
        this.cache = cache;
        this.codec = codec;
        this.generator = generator;
        this.properties = properties;
    }

    @Override
    public Mono<AccountPasswordLoginContext.Signature.Dto> execute(
            AuthVoucherContext voucher, AccountPasswordLoginContext.Signature.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getAccountPassword().isEnable()
                || !properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                    this.getClass(),
                    "fun execute(AuthVoucherContext voucher,AccountPasswordLoginContext.Signature.Request param)",
                    "Account password signature service not enabled exception."
            ));
        }
        final AuthAccountPasswordLoginTransmissionCodec.Model cm = codec.generate();
        final String cv = JsonUtil.toJson(cm);
        final String ck = generator.execute();
        final Map<String, String> map = new HashMap<>(2);
        map.put(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_MARK, ck);
        map.put(AuthVoucherContext.ACCOUNT_PASSWORD_CODEC_DATE, String.valueOf(System.currentTimeMillis()));
        return cache
                .set(ck, cv)
                .flatMap(b -> b ? Mono.just(true) : Mono.empty())
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeCacheException(
                        this.getClass(),
                        "fun execute(AuthVoucherContext voucher,AccountPasswordLoginContext.Signature.Request param)",
                        "Voucher write cache [cache.set()] exception."
                )))
                .flatMap(b -> voucher.set(map))
                .map(m -> new AccountPasswordLoginContext.Signature.Dto().setKey(cm.getPublicKey()));
    }

}
