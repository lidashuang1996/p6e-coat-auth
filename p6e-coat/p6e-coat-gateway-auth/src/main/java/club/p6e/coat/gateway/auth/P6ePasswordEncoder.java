package club.p6e.coat.gateway.auth;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6ePasswordEncoder implements PasswordEncoder {

    private static final int M = 16;
    private static final BCryptPasswordEncoder B_CRYPT = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else {
            final String content = B_CRYPT.encode(rawPassword);
            final int mData = ((int) content.charAt(content.length() - 1)) % M;
            return DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(
                    content.substring(0, mData).getBytes(StandardCharsets.UTF_8)) + content.substring(mData)
            ).getBytes(StandardCharsets.UTF_8));
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("rawPassword cannot be null");
        } else if (encodedPassword != null && encodedPassword.length() != 0) {
            return encode(rawPassword).equals(encodedPassword);
        } else {
            return false;
        }
    }
}
