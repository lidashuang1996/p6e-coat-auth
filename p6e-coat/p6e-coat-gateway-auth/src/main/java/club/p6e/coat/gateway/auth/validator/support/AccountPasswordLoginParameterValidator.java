package club.p6e.coat.gateway.auth.validator.support;

import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.utils.VerificationUtil;
import club.p6e.coat.gateway.auth.validator.ParameterValidatorInterface;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 账号密码登录的参数验证器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnExpression(AccountPasswordLoginParameterValidator.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginParameterValidator implements ParameterValidatorInterface {

    /**
     * 执行顺序
     */
    private static final int ORDER = 0;

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} && ${p6e.auth.login.account-password.enable:false}}";

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
        System.out.println("-------------------------");
        System.out.println(data);
        System.out.println("-------------------------");
        if (data instanceof final LoginContext.AccountPassword.Request param) {
            System.out.println("-------------------------");
            System.out.println(data);
            System.out.println("-------------------------");
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
                        System.out.println(param.getAccount());
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
