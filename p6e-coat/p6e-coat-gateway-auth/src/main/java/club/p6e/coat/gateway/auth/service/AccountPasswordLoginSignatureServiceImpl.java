package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.codec.AccountPasswordLoginTransmissionCodec;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.AccountPasswordLoginSignatureGenerator;
import club.p6e.coat.gateway.auth.utils.JsonUtil;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.HashMap;
import java.util.Map;

/**
 * 账号密码登录的密码签名服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginSignatureServiceImpl implements AccountPasswordLoginSignatureService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 传输编码解码器
     */
    private final AccountPasswordLoginTransmissionCodec codec;

    private final AccountPasswordLoginSignatureCache cache;

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
            AccountPasswordLoginTransmissionCodec codec) {
        this.codec = codec;
        this.cache = cache;
        this.generator = generator;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable();
    }

    @Override
    public Mono<LoginContext.AccountPasswordSignature.Dto> execute(
            ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>");
        return Mono
                .just(isEnable())
                .flatMap(b -> {
                    if (b) {
                        return AuthVoucher
                                .init(exchange)
                                .flatMap(v -> {
                                    final String mark = generator.execute();
                                    final AccountPasswordLoginTransmissionCodec.Model model = codec.generate();
                                    final Map<String, String> map = new HashMap<>(2);
                                    map.put(AuthVoucher.ACCOUNT_PASSWORD_CODEC_MARK, mark);
                                    map.put(AuthVoucher.ACCOUNT_PASSWORD_CODEC_DATE, String.valueOf(System.currentTimeMillis()));
                                    return cache
                                            .set(mark, JsonUtil.toJson(model))
                                            .flatMap(cb -> cb ? v.set(map) : Mono.error(GlobalExceptionContext.executeCacheException(
                                                    this.getClass(),
                                                    "fun execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param)",
                                                    "Account password login signature cache service exception."
                                            )))
                                            .map(rv -> new LoginContext.AccountPasswordSignature.Dto().setContent(model.getPublicKey()));
                                });
                    } else {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param)",
                                "Account password login signature service not enabled exception."
                        ));
                    }
                });
    }
}
