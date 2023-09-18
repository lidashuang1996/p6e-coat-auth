package club.p6e.auth.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author lidashuang
 * @version 1.0
 */
@SuppressWarnings("ALL")
public final class AesUtil {

    /**
     * 定义类
     */
    public interface Definition {

        /**
         * 加密
         *
         * @param key     密钥
         * @param content 明文
         * @return 密文
         */
        public String encryption(String key, String content);

        /**
         * 解密
         *
         * @param key     密钥
         * @param content 密文
         * @return 明文
         */
        public String decryption(String key, String content);
    }

    /**
     * 实现类
     */
    private static class Implementation implements Definition {

        private static final String ALGORITHM = "AES";
        private static final String CHARSET = "UTF-8";

        @Override
        public String encryption(String key, String content) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
                final byte[] encryptedBytes = cipher.doFinal(content.getBytes(CHARSET));
                return Base64.getEncoder().encodeToString(encryptedBytes);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        public String decryption(String key, String content) {
            try {
                final SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), ALGORITHM);
                final Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
                final byte[] encryptedBytes = Base64.getDecoder().decode(content);
                final byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
                return new String(decryptedBytes, CHARSET);
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * 默认的 AES 实现的对象
     */
    private static Definition DEFINITION = new Implementation();

    /**
     * 设置 AES 实现的对象
     *
     * @param implementation AES 实现的对象
     */
    public static void set(Definition implementation) {
        DEFINITION = implementation;
    }

    /**
     * 加密
     *
     * @param key     密钥
     * @param content 明文
     * @return 密文
     */
    public static String encryption(String key, String content) {
        return DEFINITION.encryption(key, content);
    }

    /**
     * 解密
     *
     * @param key     密钥
     * @param content 密文
     * @return 明文
     */
    public static String decryption(String key, String content) {
        return DEFINITION.decryption(key, content);
    }

}
