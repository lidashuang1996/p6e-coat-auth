package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.model.UserModel;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * 用户查询服务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ReactiveUserDetailsService.class,
        ignored = AuthReactiveUserDetailsServiceImpl.class
)
public class AuthReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final Properties properties;
    private final UserRepository userRepository;
    private final UserAuthRepository userAuthRepository;

    public AuthReactiveUserDetailsServiceImpl(Properties properties, UserRepository userRepository, UserAuthRepository userAuthRepository) {
        this.properties = properties;
        this.userRepository = userRepository;
        this.userAuthRepository = userAuthRepository;
    }

    @Override
    public Mono<UserDetails> findByUsername(String content) {
        final Mono<UserModel> mono;
        final Properties.Mode mode = properties.getMode();
        switch (mode) {
            case ACCOUNT -> mono = userRepository.findOneByAccount(content);
            case PHONE -> mono = userRepository.findOneByPhone(content);
            case MAILBOX -> mono = userRepository.findOneByMailbox(content);
            case PHONE_OR_MAILBOX -> mono = userRepository.findOneByPhoneOrMailbox(content);
            default -> throw new RuntimeException();
        }
        return mono
                .flatMap(um ->
                        userAuthRepository
                                .findOneById(um.getId())
                                .map(uam -> (UserDetails) new AuthUserDetails(content, um, uam)));
    }

}
