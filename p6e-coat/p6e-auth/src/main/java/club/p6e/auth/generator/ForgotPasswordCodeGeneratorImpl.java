package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordCodeGeneratorImpl implements ForgotPasswordCodeGenerator {
    @Override
    public String execute() {
        return GeneratorUtil.random();
    }
}
