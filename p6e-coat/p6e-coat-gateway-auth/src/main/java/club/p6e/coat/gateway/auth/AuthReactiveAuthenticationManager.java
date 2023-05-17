package club.p6e.coat.gateway.auth;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


/**
 * 认证管理
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthReactiveAuthenticationManager implements ReactiveAuthenticationManager {

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        System.out.println("xxxxxxxxxxxxxxxxxxxx");
        // 禁用此处认证
        return Mono.error(new RuntimeException("Not support authentication."));
    }

}
