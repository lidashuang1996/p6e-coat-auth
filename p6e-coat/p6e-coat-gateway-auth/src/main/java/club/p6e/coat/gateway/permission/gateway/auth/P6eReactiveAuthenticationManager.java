package club.p6e.coat.gateway.permission.gateway.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6eReactiveAuthenticationManager implements ReactiveAuthenticationManager  {

    private final PasswordEncoder passwordEncoder;
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    public P6eReactiveAuthenticationManager(
            PasswordEncoder passwordEncoder,
            ReactiveUserDetailsService reactiveUserDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.reactiveUserDetailsService = reactiveUserDetailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        System.out.println("authentication" + authentication);
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        System.out.println("password " + password);
        System.out.println("db password " + passwordEncoder.encode(password));
        return reactiveUserDetailsService.findByUsername(username)
                .filter(userDetails -> true)
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

}
