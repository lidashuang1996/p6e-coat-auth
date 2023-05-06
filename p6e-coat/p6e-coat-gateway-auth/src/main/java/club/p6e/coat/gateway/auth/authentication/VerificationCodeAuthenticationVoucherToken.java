package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * @author lidashuang
 * @version 1.0
 */
public class VerificationCodeAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    public VerificationCodeAuthenticationVoucherToken(String voucher, String key, String value) {
        super(
                voucher,
                new VerificationCodeAuthenticationVoucherToken.KeyValueCredentialsContainer(key),
                new VerificationCodeAuthenticationVoucherToken.KeyValueCredentialsContainer(value)
        );
    }

    public static VerificationCodeAuthenticationVoucherToken create(String voucher, String key, String value) {
        return new VerificationCodeAuthenticationVoucherToken(voucher, key, value);
    }

    public static class KeyValueCredentialsContainer implements CredentialsContainer {

        private String content;

        public KeyValueCredentialsContainer(String content) {
            this.content = content;
        }

        @Override
        public void eraseCredentials() {
            content = null;
        }

        @Override
        public String toString() {
            return content;
        }

    }

}
