package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.context.ResultContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 验证登录的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = VerificationLoginController.class,
//        ignored = VerificationLoginControllerDefaultImpl.class
//)
//@ConditionalOnExpression(VerificationLoginController.CONDITIONAL_EXPRESSION)
public class VerificationLoginControllerDefaultImpl
        implements VerificationLoginController<LoginContext.Verification.Request, AuthUserDetails> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法
     *
     * @param fm         外交部对象
     * @param aspects    验证登录的切面对象
     * @param properties 配置文件对象
     */
    public VerificationLoginControllerDefaultImpl(
            Properties properties) {
        this.properties = properties;
    }


    @Override
    public Mono<AuthUserDetails> execute(LoginContext.Verification.Request param) {
        return null;
    }
}
