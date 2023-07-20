package club.p6e.cloud.auth.generator;

import club.p6e.cloud.auth.utils.GeneratorUtil;

/**
 * @author lidashuang
 * @version 1.0
 */
public class RegisterOtherLoginGeneratorImpl implements RegisterOtherLoginGenerator{
    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }
}
