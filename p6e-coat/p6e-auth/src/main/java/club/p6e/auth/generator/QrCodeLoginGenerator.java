package club.p6e.auth.generator;

/**
 * 二维码登录
 * 二维码生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QrCodeLoginGenerator {

    /**
     * 生成二维码
     *
     * @return 二维码序号
     */
    public String execute();

}
