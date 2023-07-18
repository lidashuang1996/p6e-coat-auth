package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2CodeGeneratorDefaultImpl implements Oauth2CodeGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
