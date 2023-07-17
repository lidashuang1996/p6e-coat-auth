package club.p6e.coat.gateway.auth.utils;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class RsaUtil {

    /**
     * 私钥加密
     */
    public static String encryptionByPrivateKey(String privateKeyText, String text) throws Exception {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyText));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        return new String(cipher.doFinal(text.getBytes()), StandardCharsets.UTF_8);
    }

    /**
     * 私钥解密
     */
    public static String decryptionByPrivateKey(String privateKeyText, String text) throws Exception {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec5 =
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyText));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec5);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(text)), StandardCharsets.UTF_8);
    }

    /**
     * 公钥加密
     */
    public static String encryptionByPublicKey(String publicKeyText, String text) throws Exception {
        final X509EncodedKeySpec x509EncodedKeySpec =
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyText));
        final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        final PublicKey publicKey = keyFactory.generatePublic(x509EncodedKeySpec);
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 构建RSA密钥对
     */
    public static KeyModel generateKeyPair() throws Exception {
        final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        final KeyPair keyPair = keyPairGenerator.generateKeyPair();
        final RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) keyPair.getPrivate();
        return new KeyModel(
                Base64.getEncoder().encodeToString(rsaPublicKey.getEncoded()),
                Base64.getEncoder().encodeToString(rsaPrivateKey.getEncoded()));
    }


    /**
     * RSA密钥对对象
     */
    @Data
    @Accessors(chain = true)
    @SuppressWarnings("all")
    public static class KeyModel {
        private final String publicKey;
        private final String privateKey;

        public KeyModel(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }
}
