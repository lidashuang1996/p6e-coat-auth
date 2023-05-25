package club.p6e.coat.gateway.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthMarkAuthentication extends AbstractAuthenticationToken implements Authentication {

    private final String uid;
    private final String content;

    public AuthMarkAuthentication(String uid, String content, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.uid = uid;
        this.content = content;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return content;
    }

    @Override
    public Object getPrincipal() {
        return uid;
    }

}
