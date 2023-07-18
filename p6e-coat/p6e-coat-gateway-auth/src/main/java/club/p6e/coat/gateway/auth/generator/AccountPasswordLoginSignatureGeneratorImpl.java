package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginSignatureGeneratorImpl implements AccountPasswordLoginSignatureGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
