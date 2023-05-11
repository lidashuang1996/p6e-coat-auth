package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.authentication.AccountPasswordAuthenticationVoucherToken;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证管理
 *
 * @author lidashuang
 * @version 1.0
 */
//@Component
//@ConditionalOnMissingBean(
//        value = ReactiveAuthenticationManager.class,
//        ignored = AuthReactiveAuthenticationManager.class
//)
public class AuthReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    /**
     * 密码编码器对象
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户查询服务对象
     */
    private final ReactiveUserDetailsService reactiveUserDetailsService;

    private final Map<Class<? extends Authentication>, ReactiveAuthenticationManager> map = Collections.synchronizedMap(new HashMap<>());

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
        this.map.put(AccountPasswordAuthenticationVoucherToken.class, createAccountPasswordReactiveAuthenticationManager());
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        for (final Class<? extends Authentication> key : map.keySet()) {
            if (key.isAssignableFrom(authentication.getClass())) {
                return map.get(key).authenticate(authentication);
            }
        }
        return null;
    }

    protected ReactiveAuthenticationManager createAccountPasswordReactiveAuthenticationManager() {
        return authentication -> {
            if (authentication instanceof final AccountPasswordAuthenticationVoucherToken accountPasswordAuthentication) {
                final String username = accountPasswordAuthentication.getName();
                final String password = accountPasswordAuthentication.getCredentials().toString();
                accountPasswordAuthentication.eraseCredentials();
                return reactiveUserDetailsService
                        .findByUsername(username)
                        .filter(u -> u.getPassword().equals(passwordEncoder.encode(password)))
                        .flatMap(details -> {
                            if (details instanceof final AuthUserDetails authUserDetails) {
                                System.out.println(details);
                                return Mono.just(
                                        new JsonSerializeDeserializeAuthentication(
                                                String.valueOf(authUserDetails.getId()),
                                                false,
                                                authUserDetails.toMap()
                                        )
                                );
                            } else {
                                return Mono.error(new RuntimeException());
                            }
                        });
            }
            return null;
        };
    }

}
