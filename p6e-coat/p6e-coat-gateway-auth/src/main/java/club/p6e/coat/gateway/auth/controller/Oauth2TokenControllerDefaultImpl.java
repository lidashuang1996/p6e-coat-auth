package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.context.ResultContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * OAUTH2 TOKEN
 * 进行认证的进一步操作
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
//@ConditionalOnMissingBean(
//        value = Oauth2TokenController.class,
//        ignored = Oauth2TokenControllerDefaultImpl.class
//)
@ConditionalOnExpression(Oauth2TokenController.CONDITIONAL_EXPRESSION)
public class Oauth2TokenControllerDefaultImpl
        implements Oauth2TokenController<Oauth2Context.Token.Request, ResultContext> {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH2 TOKEN 服务
     */
    private final Oauth2TokenService service;

    /**
     * OAUTH2 TOKEN 切面
     */
    private final List<Oauth2TokenAspectService> aspects;

    /**
     * 构造方法
     *
     * @param properties 配置文件对象
     * @param aspects    OAUTH2 TOKEN 切面
     * @param service    OAUTH2 TOKEN 服务
     */
    public Oauth2TokenControllerDefaultImpl(
            Properties properties,
            Oauth2TokenService service,
            List<Oauth2TokenAspectService> aspects) {
        this.service = service;
        this.aspects = aspects;
        this.properties = properties;
    }

    @Override
    public ResultContext execute(Oauth2Context.Token.Request param) {
        // 读取配置文件判断服务是否启动
        if (!properties.getOauth2().isEnable()
                || (!properties.getOauth2().getClient().isEnable()
                && !properties.getOauth2().getPassword().isEnable()
                && !properties.getOauth2().getAuthorizationCode().isEnable())) {
            throw GlobalExceptionContext.executeOauth2ServiceNotEnableException(
                    this.getClass(), "fun execute(Oauth2Context.Token.Request param).");
        }

        // 获取 HttpServletRequest/HttpServletResponse 对象
        final HttpServletRequest request = getRequest();
        final HttpServletResponse response = getResponse();

        // 验证参数
        if (!ParameterValidator.execute(request, param)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(), "fun execute(Oauth2Context.Token.Request param).");
        } else {
            final Object result = execute(aspects, param, request, response, joinPoint -> {
                if (joinPoint.getParam() instanceof final Oauth2Context.Token.Request jpParam) {
                    joinPoint.setResult(CopyUtil.run(service.execute(jpParam), Oauth2Context.Token.Vo.class));
                } else {
                    throw GlobalExceptionContext.executeTypeMismatchException(this.getClass(),
                            "fun execute(String voucher, Oauth2Context.Token.Request param)."
                                    + " expect: " + Oauth2Context.Token.Request.class
                                    + " obtain: " + joinPoint.getParam().getClass());
                }
                return true;
            });

            // 结果处理
            return result == null ? null : ResultContext.build(result);
        }
    }

}
