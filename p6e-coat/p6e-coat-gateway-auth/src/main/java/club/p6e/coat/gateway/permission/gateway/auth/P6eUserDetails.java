package club.p6e.coat.gateway.permission.gateway.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6eUserDetails extends User implements UserDetails {


    public P6eUserDetails(String username, String password,
                          Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public P6eUserDetails(String username, String password, boolean enabled,
                          boolean accountNonExpired, boolean credentialsNonExpired,
                          boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public P6eUserDetails(UserDetails user) {
        super(user.getUsername(), user.getPassword(), user.getAuthorities());
    }

}
