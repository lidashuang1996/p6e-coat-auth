package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthCertificateInterceptor;
import club.p6e.coat.gateway.auth.AuthUserDetails;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.LoginContext;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import org.springframework.web.server.ServerWebExchange;
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
public class VerificationLoginControllerImpl
        implements VerificationLoginController<LoginContext.Verification.Request, AuthUserDetails> {

    /**
     * 用户信息的请求头名称
     */
    private static final String USER_HEADER_NAME = "P6e-User-Info";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 认证证书拦截器对象
     */
    private final AuthCertificateInterceptor interceptor;

    /**
     * 构造方法
     *
     * @param properties  配置文件对象
     * @param interceptor 验证登录的切面对象
     */
    public VerificationLoginControllerImpl(Properties properties, AuthCertificateInterceptor interceptor) {
        this.properties = properties;
        this.interceptor = interceptor;
    }

    /**
     * 判断是否启用
     *
     * @return 是否启用
     */
    protected boolean isEnable() {
        return properties.getLogin().isEnable();
    }

    @Override
    public Mono<AuthUserDetails> execute(ServerWebExchange exchange, LoginContext.Verification.Request param) {
        return Mono
                .just(isEnable())
                .flatMap(b -> {
                    if (b) {
                        return interceptor
                                .execute(exchange)
                                .flatMap(e -> {
                                    final ServerHttpRequest request = e.getRequest();
                                    final HttpHeaders httpHeaders = request.getHeaders();
                                    final List<String> list = httpHeaders.get(USER_HEADER_NAME);
                                    if (list != null && list.size() > 0) {
                                        return Mono.just(new AuthUserDetails(list.get(0)));
                                    } else {
                                        return Mono.error(GlobalExceptionContext.exceptionAuthException(
                                                this.getClass(),
                                                "fun execute(ServerWebExchange exchange, LoginContext.Verification.Request param).",
                                                "Verification login authentication exception."
                                        ));
                                    }
                                });
                    } else {
                        return Mono.error(GlobalExceptionContext.executeServiceNotEnabledException(
                                this.getClass(),
                                "fun execute(ServerWebExchange exchange, LoginContext.Verification.Request param).",
                                "Verification login service not enabled exception."
                        ));
                    }
                });
    }
}
