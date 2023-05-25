//package club.p6e.coat.gateway.auth.controller;
//
//import club.p6e.coat.gateway.auth.Properties;
//import club.p6e.coat.gateway.auth.context.LoginContext;
//import club.p6e.coat.gateway.auth.context.ResultContext;
//import club.p6e.coat.gateway.auth.service.QrCodeObtainService;
//import club.p6e.coat.gateway.auth.validator.ParameterValidator;
//import org.springframework.stereotype.Component;
//
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 二维码登录获取实现
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
////@ConditionalOnMissingBean(
////        value = QrCodeObtainController.class,
////        ignored = QrCodeObtainControllerDefaultImpl.class
////)
////@ConditionalOnExpression(QrCodeObtainController.CONDITIONAL_EXPRESSION)
//public class QrCodeObtainControllerDefaultImpl
//        implements QrCodeObtainController<LoginContext.QrCodeObtain.Request, ResultContext> {
//
//    /**
//     * 配置文件对象
//     */
//    private final Properties properties;
//
//    /**
//     * 二维码获取服务
//     */
//    private final QrCodeObtainService service;
//
//    /**
//     * 构造方法
//     *
//     * @param properties 配置文件对象
//     * @param service    二维码获取的服务对象
//     */
//    public QrCodeObtainControllerDefaultImpl(
//            Properties properties,
//            QrCodeObtainService service) {
//        this.service = service;
//        this.properties = properties;
//    }
//
//    protected boolean isEnable() {
//        return properties.getLogin().isEnable()
//                && properties.getLogin().getQrCode().isEnable();
//    }
//
//    protected Mono<Boolean> validateParameter(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param) {
//        return ParameterValidator.execute(exchange, param);
//    }
//
//    @Override
//    public Mono<ResultContext> execute(ServerWebExchange exchange, LoginContext.QrCodeObtain.Request param) {
//        return Mono
//                .just(isEnable())
//                .flatMap(b -> b ? validateParameter(exchange, param) : Mono.error(new RuntimeException()))
//                .flatMap(b -> b ? Mono.just(param) : Mono.error(new RuntimeException()))
//                .flatMap(service::execute)
//                .map(ResultContext::build);
//    }
//}