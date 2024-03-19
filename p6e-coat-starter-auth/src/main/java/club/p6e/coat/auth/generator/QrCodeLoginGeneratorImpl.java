package club.p6e.coat.auth.generator;

import club.p6e.coat.common.utils.GeneratorUtil;

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
        return (GeneratorUtil.uuid() + GeneratorUtil.random(12, true, false)).toLowerCase();
    }

}
