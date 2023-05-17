package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 二维码登录
 * 二维码生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = QrCodeLoginGenerator.class,
        ignored = QrCodeLoginGeneratorDefaultImpl.class
)
@ConditionalOnExpression(QrCodeLoginGenerator.CONDITIONAL_EXPRESSION)
public class QrCodeLoginGeneratorDefaultImpl implements QrCodeLoginGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }
}
