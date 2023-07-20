package club.p6e.cloud.auth.generator;

/**
 * 凭证会话
 * 凭证会话序号生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VoucherGenerator {

    /**
     * 生成会话序号
     *
     * @return 会话序号
     */
    public String execute();

}
