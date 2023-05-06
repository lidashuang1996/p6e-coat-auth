package club.p6e.coat.gateway.auth.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AbstractAuthenticationVoucherToken extends AbstractAuthenticationToken {

    private final String voucher;
    private final Object principal;
    private final Object credentials;

    public AbstractAuthenticationVoucherToken(String voucher, Object principal, Object credentials) {
        super(null);
        this.voucher = voucher;
        this.principal = principal;
        this.credentials = credentials;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    public String getVoucher() {
        return voucher;
    }
}
