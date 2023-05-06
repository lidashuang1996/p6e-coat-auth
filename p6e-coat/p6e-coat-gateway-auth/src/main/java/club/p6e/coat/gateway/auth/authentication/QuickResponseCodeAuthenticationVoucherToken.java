package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * @author lidashuang
 * @version 1.0
 */
public class QuickResponseCodeAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    public QuickResponseCodeAuthenticationVoucherToken(String voucher, String content) {
        super(voucher, null, new QuickResponseCodeAuthenticationVoucherToken.ContentCredentialsContainer(content));
    }

    public QuickResponseCodeAuthenticationVoucherToken(String voucher, Object principal, Object credentials) {
        super(voucher, principal, credentials);
    }

    public static QuickResponseCodeAuthenticationVoucherToken create(String voucher, String content) {
        return new QuickResponseCodeAuthenticationVoucherToken(voucher, content);
    }

    public static class ContentCredentialsContainer implements CredentialsContainer {

        private String content;

        public ContentCredentialsContainer(String content) {
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
