package club.p6e.auth.generator;


import club.p6e.auth.utils.GeneratorUtil;

/**
 * 忘记密码
 * 验证码生成器实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class ForgotPasswordCodeGeneratorImpl implements ForgotPasswordCodeGenerator {

    @Override
    public String execute(String type) {
        if (type.equalsIgnoreCase("SMS")) {
            return GeneratorUtil.random(6, false, false).toLowerCase();
        }
        if (type.equalsIgnoreCase("EMAIL")) {
            return GeneratorUtil.random(6, true, false).toUpperCase();
        }
        return "";
    }

}
