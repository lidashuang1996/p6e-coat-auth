package club.p6e.cloud.auth.validator;

import club.p6e.cloud.auth.utils.VerificationUtil;
import club.p6e.cloud.auth.Properties;
import club.p6e.cloud.auth.context.LoginContext;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的参数验证器
 *
 * @author lidashuang
 * @version 1.0
 */
public class AccountPasswordLoginParameterValidator implements ParameterValidatorInterface {

    /**
     * 执行顺序
     */
    private static final int ORDER = 0;

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public AccountPasswordLoginParameterValidator(Properties properties) {
        this.properties = properties;
    }

    @Override
    public int order() {
        return ORDER;
    }

    @Override
    public Class<?> select() {
        return LoginContext.AccountPassword.Request.class;
    }

    @Override
    public Mono<Boolean> execute(ServerWebExchange request, Object data) {
        if (data instanceof final LoginContext.AccountPassword.Request param) {
            if (param.getAccount() == null || param.getPassword() == null) {
                return Mono.just(false);
            } else {
                final Properties.Mode mode = properties.getMode();
                switch (mode) {
                    case PHONE -> {
                        if (VerificationUtil.phone(param.getAccount())) {
                            return Mono.just(true);
                        }
                    }
                    case MAILBOX -> {
                        if (VerificationUtil.mailbox(param.getAccount())) {
                            return Mono.just(true);
                        }
                    }
                    case PHONE_OR_MAILBOX -> {
                        if (VerificationUtil.phone(param.getAccount())
                                || VerificationUtil.mailbox(param.getAccount())) {
                            return Mono.just(true);
                        }
                    }
                    default -> {
                        return Mono.just(false);
                    }
                }
            }
        }
        return Mono.just(false);
    }

}
