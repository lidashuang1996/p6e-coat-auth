package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.QrCodeLoginGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
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
@Component
//@ConditionalOnMissingBean(
//        value = QrCodeObtainService.class,
//        ignored = QrCodeObtainServiceDefaultImpl.class
//)
//@ConditionalOnExpression(QrCodeObtainService.CONDITIONAL_EXPRESSION)
public class QrCodeObtainServiceDefaultImpl implements QrCodeObtainService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

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
    public QrCodeObtainServiceDefaultImpl(
            Properties properties,
            QrCodeLoginCache cache,
            QrCodeLoginGenerator generator) {
        this.cache = cache;
        this.generator = generator;
        this.properties = properties;
    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable();
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
