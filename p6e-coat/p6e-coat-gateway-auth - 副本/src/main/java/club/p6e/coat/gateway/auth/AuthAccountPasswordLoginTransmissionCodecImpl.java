package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.utils.RsaUtil;
import org.springframework.stereotype.Component;

/**
 * 账号密码登录传输数据编码解码器的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthAccountPasswordLoginTransmissionCodecImpl implements AuthAccountPasswordLoginTransmissionCodec {

    @Override
    public Model generate() {
        try {
            final RsaUtil.KeyModel keyModel = RsaUtil.generateKeyPair();
            return new Model()
                    .setPublicKey(keyModel.getPublicKey())
                    .setPrivateKey(keyModel.getPrivateKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encrypt(Model model, String content) {
        String mark = null;
        try {
            if (model == null) {
                throw new RuntimeException();
            } else {
                if (model.getPublicKey() != null) {
                    return RsaUtil.encryptByPublicKey(model.getPublicKey(), content);
                } else if (model.getPrivateKey() != null) {
                    return RsaUtil.encryptByPrivateKey(model.getPrivateKey(), content);
                } else {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String decrypt(Model model, String content) {
        String mark = null;
        try {
            if (model == null) {
                throw new RuntimeException();
            } else {
                if (model.getPrivateKey() != null) {
                    return RsaUtil.decryptByPrivateKey(model.getPrivateKey(), content);
                } else {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
