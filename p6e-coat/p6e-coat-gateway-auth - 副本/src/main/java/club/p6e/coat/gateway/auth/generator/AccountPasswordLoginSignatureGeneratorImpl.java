package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AccountPasswordLoginSignatureGeneratorImpl implements AccountPasswordLoginSignatureGenerator {
    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }
}
