package club.p6e.auth.codec;

import club.p6e.auth.utils.RsaUtil;

/**
 * 密码编码解码器的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class PasswordTransmissionCodecImpl implements PasswordTransmissionCodec {

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
    public String encryption(Model model, String content) {
        if (model == null) {
            throw new RuntimeException("[ encryption ] model content is null exception");
        } else {
            try {
                if (model.getPublicKey() != null) {
                    return RsaUtil.publicKeyEncryption(model.getPublicKey(), content);
                } else if (model.getPrivateKey() != null) {
                    return RsaUtil.privateKeyDecryption(model.getPrivateKey(), content);
                } else {
                    throw new RuntimeException("[ encryption ] model content exception >> " + model);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String decryption(Model model, String content) {
        if (model == null) {
            throw new RuntimeException("[ decryption ] model content is null exception");
        } else {
            try {
                if (model.getPrivateKey() != null) {
                    return RsaUtil.privateKeyDecryption(model.getPrivateKey(), content);
                } else {
                    throw new RuntimeException("[ decryption ] model content exception >> " + model);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
