package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucherContext;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.generator.QrCodeLoginGenerator;
import club.p6e.coat.gateway.auth.cache.QuickResponseCodeLoginCache;
import club.p6e.coat.gateway.auth.context.QuickResponseCodeContext;
import club.p6e.coat.gateway.auth.error.ServiceNotEnabledException;
import org.springframework.stereotype.Component;
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
public class QuickResponseCodeObtainServiceImpl implements QuickResponseCodeObtainService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 二维码缓存对象
     */
    private final QuickResponseCodeLoginCache cache;

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
    public QuickResponseCodeObtainServiceImpl(
            Properties properties,
            QuickResponseCodeLoginCache cache,
            QrCodeLoginGenerator generator) {
        this.cache = cache;
        this.generator = generator;
        this.properties = properties;
    }

    @Override
    public Mono<QuickResponseCodeContext.Obtain.Dto> execute(QuickResponseCodeContext.Obtain.Request param) {
        if (!properties.getLogin().isEnable()
                || !properties.getLogin().getQrCode().isEnable()) {
            throw new ServiceNotEnabledException(
                    this.getClass(), "fun execute(LoginContext.AccountPasswordSignature.Request param).", "");
        }
        final String qrc = generator.execute();
        final AuthVoucherContext avc = param.getVoucher();
        final Map<String, String> map = new HashMap<>(2);
        map.put(AuthVoucherContext.QUICK_RESPONSE_CODE_LOGIN_MARK, qrc);
        map.put(AuthVoucherContext.QUICK_RESPONSE_CODE_LOGIN_DATE, String.valueOf(System.currentTimeMillis()));
        return cache
                .set(qrc, QuickResponseCodeLoginCache.EMPTY_CONTENT)
                .flatMap(b -> avc.set(map))
                .map(c -> new QuickResponseCodeContext.Obtain.Dto().setContent(qrc));
    }
}
