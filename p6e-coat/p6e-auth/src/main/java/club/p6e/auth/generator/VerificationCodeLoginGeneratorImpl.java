package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * 验证码登录
 * 验证码生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeLoginGeneratorImpl implements CodeLoginGenerator {

    @Override
    public String execute(String type) {
        return GeneratorUtil.random(6, false, false);
    }

}
