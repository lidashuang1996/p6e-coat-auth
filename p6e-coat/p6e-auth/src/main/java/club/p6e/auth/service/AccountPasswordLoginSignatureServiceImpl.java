package club.p6e.auth.service;

import club.p6e.auth.codec.AccountPasswordLoginTransmissionCodec;
import club.p6e.auth.utils.JsonUtil;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.auth.context.LoginContext;
import club.p6e.auth.error.GlobalExceptionContext;
import club.p6e.auth.generator.AccountPasswordLoginSignatureGenerator;
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
     * 缓存对象
     */
    private final AccountPasswordLoginSignatureCache cache;

    /**
     * 传输编码解码器
     */
    private final AccountPasswordLoginTransmissionCodec codec;

    /**
     * 标记生成器
     */
    private final AccountPasswordLoginSignatureGenerator generator;

    /**
     * 构造方法初始化
     *
     * @param codec     传输编码解码器
     * @param cache     缓存对象
     * @param generator 标记生成器对象
     */
    public AccountPasswordLoginSignatureServiceImpl(
            AccountPasswordLoginSignatureCache cache,
            AccountPasswordLoginTransmissionCodec codec,
            AccountPasswordLoginSignatureGenerator generator) {
        this.cache = cache;
        this.codec = codec;
        this.generator = generator;
    }

    @Override
    public Mono<LoginContext.AccountPasswordSignature.Dto> execute(
            ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param) {
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
    }
}