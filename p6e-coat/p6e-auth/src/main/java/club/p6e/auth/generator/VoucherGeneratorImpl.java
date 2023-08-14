package club.p6e.auth.generator;

import club.p6e.auth.utils.GeneratorUtil;

/**
 * 凭证会话序号生成器实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VoucherGeneratorImpl implements VoucherGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
