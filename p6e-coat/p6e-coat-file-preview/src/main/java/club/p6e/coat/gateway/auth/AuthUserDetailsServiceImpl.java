package club.p6e.coat.gateway.auth;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthUserDetailsServiceImpl implements UserDetailsService {

    private static final String AUTHORITIES_MARK_METHOD = "status";
    private static final String AUTHORITIES_MARK_CLASS_PATH = "club.p6e.coat.gateway.authorities.AuthoritiesMark";

    @Override
    public AuthUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AuthUserDetails(
                User
                        .builder()
                        .username("123456")
                        .password("321321")
                        .authorities(authorities())
                        .build()
        );
    }

    private String authorities() {
        try {
            final Class<?> mClass = Class.forName(AUTHORITIES_MARK_CLASS_PATH);
            final Object status = mClass.getMethod(AUTHORITIES_MARK_METHOD).invoke(mClass);
            return AUTHORITIES_MARK_CLASS_PATH + status.getClass().getName();
        } catch (Exception e) {
            return "*";
        }
    }

}
