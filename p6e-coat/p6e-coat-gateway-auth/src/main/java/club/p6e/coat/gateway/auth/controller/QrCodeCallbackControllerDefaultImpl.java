//package club.p6e.coat.gateway.auth.controller;
//
//import club.p6e.coat.gateway.auth.context.LoginContext;
//import club.p6e.coat.gateway.auth.context.ResultContext;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.stereotype.Component;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//
///**
// * 二维码登录回调实现
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
////@ConditionalOnMissingBean(
////        value = QrCodeCallbackController.class,
////        ignored = QrCodeCallbackControllerDefaultImpl.class
////)
////@ConditionalOnExpression(QrCodeCallbackController.CONDITIONAL_EXPRESSION)
//public class QrCodeCallbackControllerDefaultImpl
//        implements QrCodeCallbackController<LoginContext.QrCodeCallback.Request, ResultContext> {
//
//    /**
//     * 外交部对象
//     */
//    private final ForeignMinistry fm;
//
//    /**
//     * 配置文件对象
//     */
//    private final Properties properties;
//
//    /**
//     * 二维码登录回调服务
//     */
//    private final QrCodeCallbackService service;
//
//    /**
//     * 二维码登录回调切面对象
//     */
//    private final List<QrCodeCallbackAspectService> aspects;
//
//    /**
//     * 构造方法
//     *
//     * @param fm         外交部对象
//     * @param properties 配置文件对象
//     * @param service    二维码登录的服务对象
//     * @param aspects    二维码登录的切面对象
//     */
//    public QrCodeCallbackControllerDefaultImpl(
//            ForeignMinistry fm,
//            Properties properties,
//            QrCodeCallbackService service) {
//        this.fm = fm;
//        this.service = service;
//        this.aspects = aspects;
//        this.properties = properties;
//    }
//
//    @Override
//    public ResultContext execute(LoginContext.QrCodeCallback.Request param) {
//        // 读取配置文件判断服务是否启动
//        if (!properties.getLogin().isEnable() || !properties.getLogin().getQrCode().isEnable()) {
//            throw GlobalExceptionContext.executeServiceNotEnableException(
//                    this.getClass(), "fun execute(LoginContext.QrCodeCallback.Request param).");
//        }
//
//        // 获取 HttpServletRequest/HttpServletResponse 对象
//        final HttpServletRequest request = getRequest();
//        final HttpServletResponse response = getResponse();
//
//        // 验证参数
//        if (!ParameterValidator.execute(request, param)) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(), "fun execute(LoginContext.QrCodeCallback.Request param).");
//        } else {
//            // 执行服务
//            final Object result = execute(aspects, param, request, response, joinPoint -> {
//                if (joinPoint.getParam() instanceof final LoginContext.QrCodeCallback.Request jpParam) {
//                    final ForeignMinistryVisaTemplate foreignMinistryVisaTemplate = fm.verificationAccessToken(request);
//                    jpParam.setUid(foreignMinistryVisaTemplate.getId());
//                    joinPoint.setResult(CopyUtil.run(service.execute(jpParam), LoginContext.QrCodeCallback.Vo.class));
//                    return joinPoint.proceed();
//                } else {
//                    throw GlobalExceptionContext.executeTypeMismatchException(this.getClass(),
//                            "fun execute(LoginContext.QrCodeCallback.Request param). "
//                                    + " expect: " + LoginContext.VerificationCode.Request.class
//                                    + " obtain: " + (joinPoint.getParam() == null ? null
//                                    : (joinPoint.getParam().getClass() + "[ " + joinPoint.getParam() + " ]")));
//                }
//            });
//
//            // 结果处理
//            return result == null ? null : ResultContext.build(result);
//        }
//    }
//
//    @Override
//    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCodeCallback.Request param) {
//        return null;
//    }
//}
