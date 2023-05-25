package club.p6e.coat.gateway.auth.generator;

/**
 * 二维码登录
 * 二维码生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface QrCodeLoginGenerator {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.qr-code.enable:false}}";

    /**
     * 生成二维码
     *
     * @return 二维码序号
     */
    public String execute();

}
