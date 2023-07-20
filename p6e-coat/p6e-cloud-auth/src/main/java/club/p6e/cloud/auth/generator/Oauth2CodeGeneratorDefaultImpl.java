package club.p6e.cloud.auth.generator;

import club.p6e.cloud.auth.utils.GeneratorUtil;

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
