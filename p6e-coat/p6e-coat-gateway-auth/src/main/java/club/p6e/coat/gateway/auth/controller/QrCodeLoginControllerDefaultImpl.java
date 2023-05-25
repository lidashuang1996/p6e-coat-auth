//package club.p6e.coat.gateway.auth.controller;
//
//import club.p6e.coat.gateway.auth.AuthUserDetails;
//import club.p6e.coat.gateway.auth.context.LoginContext;
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
// * 二维码登录实现
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
//@ConditionalOnMissingBean(
//        value = QrCodeLoginController.class,
//        ignored = QrCodeLoginControllerDefaultImpl.class
//)
//@ConditionalOnExpression(QrCodeLoginController.CONDITIONAL_EXPRESSION)
//public class QrCodeLoginControllerDefaultImpl
//        implements QrCodeLoginController<LoginContext.QrCode.Request, AuthUserDetails> {
//
//    /**
//     * 二维码登录获取信息为空的返回
//     */
//    private static final String QR_CODE_EMPTY_RESULT = "NO_DATA";
//
//    /**
//     * 配置文件对象
//     */
//    private final Properties properties;
//
//    /**
//     * 二维码登录服务
//     */
//    private final QrCodeLoginService service;
//
//    /**
//     * 二维码登录切面对象
//     */
//    private final List<QrCodeLoginAspectService> aspects;
//
//    /**
//     * 构造方法
//     *
//     * @param properties 配置文件对象
//     * @param service    二维码登录服务
//     * @param aspects    二维码登录切面对象
//     */
//    public QrCodeLoginControllerDefaultImpl(
//            Properties properties,
//            QrCodeLoginService service,
//            List<QrCodeLoginAspectService> aspects) {
//        this.service = service;
//        this.aspects = aspects;
//        this.properties = properties;
//    }
//
//    @Override
//    public ResultContext execute(LoginContext.QrCode.Request param) {
//        // 读取配置文件判断服务是否启动
//        if (!properties.getLogin().isEnable() || !properties.getLogin().getQrCode().isEnable()) {
//            throw GlobalExceptionContext.executeServiceNotEnableException(
//                    this.getClass(), "fun execute(LoginContext.QrCode.Request param).");
//        }
//
//        // 获取 HttpServletRequest/HttpServletResponse 对象
//        final HttpServletRequest request = getRequest();
//        final HttpServletResponse response = getResponse();
//
//        // 验证参数
//        if (!ParameterValidator.execute(request, param)) {
//            throw new ParameterException(this.getClass(),
//                    "fun execute(String voucher, LoginContext.QRCode.Request param).");
//        } else {
//            // 执行服务
//            final Object result = execute(aspects, param, request, response, joinPoint -> {
//                if (joinPoint.getParam() instanceof final LoginContext.QrCode.Request jpParam) {
//                    // 执行得到服务的结果
//                    final LoginContext.QrCode.Dto dto = service.execute(jpParam);
//                    if (dto == null) {
//                        // 返回空的数据模版
//                        joinPoint.setResult(QR_CODE_EMPTY_RESULT);
//                    } else {
//                        // 将结果转换为通用的模版（外交部的护照对象）
//                        final ForeignMinistryVisaTemplate template =
//                                new ForeignMinistryVisaTemplate(String.valueOf(dto.getId()));
//                        template.putAll(CopyUtil.toMap(dto));
//                        // 将得到的通用模版进行返回
//                        joinPoint.setResult(template);
//                    }
//                    return joinPoint.proceed();
//                } else {
//                    throw GlobalExceptionContext.executeTypeMismatchException(this.getClass(),
//                            "fun execute(String voucher, LoginContext.QRCode.Request param). "
//                                    + " expect: " + LoginContext.QrCode.Request.class
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
//    public Mono<AuthUserDetails
//
//
//            > execute(ServerWebExchange exchange, LoginContext.QrCode.Request param) {
//        return null;
//    }
//}
