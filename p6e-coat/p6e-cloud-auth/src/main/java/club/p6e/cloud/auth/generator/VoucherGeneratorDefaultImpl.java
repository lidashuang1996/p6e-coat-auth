package club.p6e.cloud.auth.generator;

import club.p6e.cloud.auth.utils.GeneratorUtil;

/**
 * 凭证会话
 * 凭证会话序号生成器默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class VoucherGeneratorDefaultImpl implements VoucherGenerator {

    @Override
    public String execute() {
        return GeneratorUtil.uuid();
    }

}
