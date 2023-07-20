package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * 二维码登录
 * 二维码生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class QrCodeLoginGeneratorDefaultImpl implements QrCodeLoginGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }
}
