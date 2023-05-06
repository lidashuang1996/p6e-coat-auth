package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserAuthModel;
import club.p6e.coat.gateway.auth.model.UserModel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthUserDetails implements UserDetails {

    public AuthUserDetails(String content, UserModel um, UserAuthModel uam) {
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
