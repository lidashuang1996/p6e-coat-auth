package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * 第三方登录需要注册的时候的凭证码生成器的实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class RegisterOtherLoginGeneratorImpl implements RegisterOtherLoginGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
