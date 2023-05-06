package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.core.CredentialsContainer;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordAuthenticationVoucherToken extends AbstractAuthenticationVoucherToken {

    public AccountPasswordAuthenticationVoucherToken(String voucher, String account, String password) {
        super(voucher, account, new PasswordCredentialsContainer(password));
    }

    public static AccountPasswordAuthenticationVoucherToken create(String voucher, String account, String password) {
        return new AccountPasswordAuthenticationVoucherToken(voucher, account, password);
    }

    public static class PasswordCredentialsContainer implements CredentialsContainer {

        private String content;

        public PasswordCredentialsContainer(String content) {
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
