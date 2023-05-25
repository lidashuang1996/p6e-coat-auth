package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 凭证会话
 * 凭证会话序号生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class VoucherGeneratorDefaultImpl implements VoucherGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
