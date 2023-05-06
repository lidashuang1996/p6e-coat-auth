package club.p6e.coat.gateway.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 认证管理
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ReactiveAuthenticationManager.class,
        ignored = AuthReactiveAuthenticationManager.class
)
public class AuthReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    /**
     * 密码编码器对象
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户查询服务对象
     */
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    /**
     * 构造方法初始化
     *
     * @param passwordEncoder            密码编码器对象
     * @param reactiveUserDetailsService 用户查询服务对象
     */
    public AuthReactiveAuthenticationManager(
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
        String password = passwordEncoder.encode(authentication.getCredentials().toString());
        System.out.println("password " + password);
        System.out.println("db password " + passwordEncoder.encode(password));
        return reactiveUserDetailsService
                .findByUsername(username)
                .filter(u -> u.getPassword().equals(password))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
    }

}
