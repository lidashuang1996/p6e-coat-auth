package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * 二维码登录
 * 二维码生成器实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginGeneratorImpl implements QrCodeLoginGenerator {

    @Override
    public String execute() {
        return (GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false)).toLowerCase();
    }

}
