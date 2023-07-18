package club.p6e.coat.gateway.auth.generator;

import club.p6e.coat.gateway.auth.utils.GeneratorUtil;

/**
 * 验证码登录
 * 验证码生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class CodeLoginGeneratorDefaultImpl implements CodeLoginGenerator {

    @Override
    public String execute(String type) {
        return GeneratorUtil.random(6, false, false);
    }

}
