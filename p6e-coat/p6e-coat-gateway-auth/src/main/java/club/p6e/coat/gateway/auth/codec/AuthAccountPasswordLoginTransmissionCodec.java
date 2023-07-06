package club.p6e.coat.gateway.auth.codec;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 账号密码登录传输数据编码解码器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface AuthAccountPasswordLoginTransmissionCodec {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{"
            + "${@{AUTH_PROPERTIES_PREFIX}.login.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable:false} "
            + "&& ${p6e.auth.login.account-password.enable-transmission-encryption:false} "
            + "}";

    /**
     * 传输编解码模型
     */
    @Data
    @Accessors(chain = true)
    class Model {
        private String publicKey;
        private String privateKey;
    }

    /**
     * 生成密钥对
     *
     * @return 密钥对模型
     */
    public Model generate();

    /**
     * 编码
     *
     * @param model   密钥对模型
     * @param content 待编码内容
     * @return 编码之后的内容
     */
    public String encrypt(Model model, String content);

    /**
     * 解码
     *
     * @param model   密钥对模型
     * @param content 待解码内容
     * @return 解码之后的内容
     */
    public String decrypt(Model model, String content);
}
