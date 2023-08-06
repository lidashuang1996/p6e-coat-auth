package club.p6e.auth.service;

import club.p6e.auth.codec.PasswordTransmissionCodec;
import club.p6e.auth.utils.JsonUtil;
import club.p6e.auth.AuthVoucher;
import club.p6e.auth.cache.PasswordSignatureCache;
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
    private final PasswordSignatureCache cache;

    /**
     * 传输编码解码器
     */
    private final PasswordTransmissionCodec codec;

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
            PasswordSignatureCache cache,
            PasswordTransmissionCodec codec,
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
                    final PasswordTransmissionCodec.Model model = codec.generate();
                    final String json = JsonUtil.toJson(model);
                    if (json == null) {
                        return Mono.error(GlobalExceptionContext.exceptionDataSerializationException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param)",
                                "Account password login signature cache data serialization exception."
                        ));
                    }
                    final String mark = generator.execute();
                    final Map<String, String> map = new HashMap<>(2);
                    map.put(AuthVoucher.ACCOUNT_PASSWORD_CODEC_MARK, mark);
                    map.put(AuthVoucher.ACCOUNT_PASSWORD_CODEC_DATE, String.valueOf(System.currentTimeMillis()));
                    return cache
                            .set(mark, json)
                            .flatMap(b -> b ? v.set(map) : Mono.error(GlobalExceptionContext.exceptionCacheWritingException(
                                    this.getClass(),
                                    "fun execute(ServerWebExchange exchange, LoginContext.AccountPasswordSignature.Request param)",
                                    "Account password login signature cache writing exception."
                            )))
                            .map(rv -> new LoginContext.AccountPasswordSignature.Dto().setContent(model.getPublicKey()));
                });
    }
}
