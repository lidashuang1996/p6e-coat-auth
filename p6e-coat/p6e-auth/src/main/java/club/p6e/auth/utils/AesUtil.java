package club.p6e.auth.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class AesUtil {

    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    public static String encrypt(String key, String content) {
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

    public static String decrypt(String key, String content) {
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
