package club.p6e.coat.gateway.auth.service;

import club.p6e.cloud.auth.Properties;
import club.p6e.cloud.auth.codec.AccountPasswordLoginTransmissionCodec;
import club.p6e.cloud.auth.context.LoginContext;
import club.p6e.cloud.auth.database.model.UserModel;
import club.p6e.cloud.auth.database.repository.UserRepository;
import club.p6e.cloud.auth.encryptor.PasswordEncryptor;
import club.p6e.cloud.auth.error.GlobalExceptionContext;
import club.p6e.cloud.auth.utils.CopyUtil;
import club.p6e.cloud.auth.utils.SpringUtil;
import club.p6e.cloud.auth.voucher.VoucherConversation;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.context.AccountPasswordLoginContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * 账号密码登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AccountPasswordLoginService.class,
        ignored = AccountPasswordLoginServiceDefaultImpl.class
)
public class AccountPasswordLoginServiceDefaultImpl implements AccountPasswordLoginService {

    private ReactiveUserDetailsService service;

    public AccountPasswordLoginServiceDefaultImpl(ReactiveUserDetailsService service) {
        this.service = service;
    }

    @Override
    public AuthUserDetails execute(AccountPasswordLoginContext.Request param) {
        final UserDetailsRepositoryReactiveAuthenticationManager manager =
                new UserDetailsRepositoryReactiveAuthenticationManager(service).authenticate();
        return null;
    }

}
