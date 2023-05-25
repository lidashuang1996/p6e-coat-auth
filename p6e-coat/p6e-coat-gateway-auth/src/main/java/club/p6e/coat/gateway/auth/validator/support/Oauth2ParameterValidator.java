//package club.p6e.coat.gateway.auth.validator.support;
//
//import club.p6e.cloud.auth.context.Oauth2Context;
//import club.p6e.cloud.auth.validator.ParameterValidatorInterface;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
//import org.springframework.stereotype.Component;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//
///**
// * 账号密码登录的参数验证器
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
//@ConditionalOnExpression(Oauth2ParameterValidator.CONDITIONAL_EXPRESSION)
//public class Oauth2ParameterValidator implements ParameterValidatorInterface {
//
//    /**
//     * 执行顺序
//     */
//    private static final int ORDER = 1000;
//
//    /**
//     * 条件注册的条件表达式
//     */
//    public final static String CONDITIONAL_EXPRESSION = "${p6e.auth.oauth2.enable:false}";
//
//    @Override
//    public int order() {
//        return ORDER;
//    }
//
//    @Override
//    public Class<?> select() {
//        return Oauth2Context.Request.class;
//    }
//
//    @Override
//    public boolean execute(HttpServletRequest request, Object data) {
//        if (data instanceof final Oauth2Context.Request param) {
//            if (param.getVoucher() == null) {
//                return false;
//            } else {
//                param.setVoucherMap(new HashMap<>());
//                return true;
//            }
//        }
//        return false;
//    }
//}
