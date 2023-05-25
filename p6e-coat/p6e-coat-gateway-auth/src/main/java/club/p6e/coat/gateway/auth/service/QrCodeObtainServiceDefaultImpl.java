package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.cache.QrCodeLoginCache;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.generator.QrCodeLoginGenerator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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

//    @Override
//    public LoginContext.QrCodeObtain.Dto execute(LoginContext.QrCodeObtain.Request param) {
//
//        // 二维码数据写入缓存
//        final String voucher = param.getVoucher();
//        final String qrCode = generator.execute();
//        cache.set(qrCode, QrCodeLoginCache.EMPTY_CONTENT);
//        // 给当前会话绑定更多数据
//        final Map<String, String> voucherContent = new HashMap<>(2);
//        voucherContent.put(VoucherConversation.QR_CODE_VALUE, qrCode);
//        voucherContent.put(VoucherConversation.QR_CODE_LOGIN, String.valueOf(System.currentTimeMillis()));
//        conversation.bind(voucher, voucherContent);
//        return new LoginContext.QrCodeObtain.Dto().setContent(qrCode);
//    }

    protected boolean isEnable() {
        return properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable();
    }

    @Override
    public Mono<LoginContext.QrCodeObtain.Dto> execute(LoginContext.QrCodeObtain.Request param) {
        return Mono.just(new LoginContext.QrCodeObtain.Dto().setContent("123321312213321"));
    }
}
