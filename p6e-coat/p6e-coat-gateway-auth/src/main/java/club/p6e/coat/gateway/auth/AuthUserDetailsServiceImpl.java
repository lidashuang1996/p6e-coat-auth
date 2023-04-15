package club.p6e.coat.gateway.auth;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        System.out.println(username);
        // 执行 DB 操作
        System.out.println("执行 DB 操作");
        return Mono.just(
                new AuthUserDetails(
                        User
                                .builder()
                                .username("123456")
                                .password("123456")
                                .authorities("*")
                                .build()
                )
        );
    }

}
