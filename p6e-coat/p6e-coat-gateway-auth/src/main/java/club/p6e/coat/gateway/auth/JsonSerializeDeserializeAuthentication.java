package club.p6e.coat.gateway.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class JsonSerializeDeserializeAuthentication implements Authentication {

    private final String id;
    private final boolean oauth2;
    private final Map<String, Object> details;
    private final Collection<? extends GrantedAuthority> authorities = AuthorityUtils.NO_AUTHORITIES;

    private boolean authenticated = true;

    public JsonSerializeDeserializeAuthentication(Authentication authentication) {
        this.id = ":1";
        this.oauth2 = false;
        this.details = null;
    }

    public JsonSerializeDeserializeAuthentication(Map<String, Object> data) {
        if (data != null
                && data.size() > 0
                && data.get("id") instanceof final String content) {
            this.id = content;
            this.oauth2 = false;
            this.details = data;
        }
        throw new RuntimeException();
    }

    public JsonSerializeDeserializeAuthentication(String id, boolean oauth2, Map<String, Object> details) {
        this.id = id;
        this.oauth2 = oauth2;
        this.details = details;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    public String getId() {
        return id;
    }

    @Override
    public Map<String, Object> getDetails() {
        return this.details;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

}
