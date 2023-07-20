package club.p6e.cloud.auth.service;

import club.p6e.cloud.auth.AuthVoucher;
import club.p6e.cloud.auth.Properties;
import club.p6e.cloud.auth.cache.QrCodeLoginCache;
import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import club.p6e.cloud.auth.generator.QrCodeLoginGenerator;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 二维码获取服务默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeObtainServiceImpl implements QrCodeObtainService {

    /**
     * 二维码缓存对象
     */
    private final QrCodeLoginCache cache;

    /**
     * 二维码生成器对象
     */
    private final QrCodeLoginGenerator generator;


    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     * @param cache      二维码缓存对象
     * @param generator  二维码生成器对象
     */
    public QrCodeObtainServiceImpl(
            Properties properties,
            QrCodeLoginCache cache,
            QrCodeLoginGenerator generator) {
        this.cache = cache;
        this.generator = generator;
    }

    @Override
    public Mono<LoginContext.QrCodeObtain.Dto> execute(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param) {
        return AuthVoucher
                .init(exchange)
                .flatMap(v -> {
                    final String qrCode = generator.execute();
                    return cache
                            .set(qrCode, QrCodeLoginCache.EMPTY_CONTENT)
                            .flatMap(b -> {
                                if (b) {
                                    final Map<String, String> map = new HashMap<>(2);
                                    map.put(AuthVoucher.QUICK_RESPONSE_CODE_LOGIN_MARK, qrCode);
                                    map.put(AuthVoucher.QUICK_RESPONSE_CODE_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
                                    return v.set(map).map(nv -> new LoginContext.QrCodeObtain.Dto().setContent(qrCode));
                                } else {
                                    return Mono.error(
                                            GlobalExceptionContext.executeCacheException(
                                                    this.getClass(),
                                                    "fun execute(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param)",
                                                    "QrCode obtain login cache data write exception."
                                            ));
                                }
                            });
                });
    }

}
