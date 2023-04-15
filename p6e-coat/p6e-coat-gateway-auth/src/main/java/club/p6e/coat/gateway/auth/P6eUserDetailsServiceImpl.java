package club.p6e.coat.gateway.auth;

import org.springframework.security.core.userdetails.*;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6eUserDetailsServiceImpl implements ReactiveUserDetailsService {

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        System.out.println(username);
        // 执行 DB 操作
        System.out.println("执行 DB 操作");
        return Mono.just(
                new P6eUserDetails(
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
