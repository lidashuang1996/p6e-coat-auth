//package club.p6e.coat.file.router;
//
//import club.p6e.coat.file.error.CustomException;
//import club.p6e.coat.file.handler.AspectHandlerFunction;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.ResponseBody;
//import reactor.core.publisher.Mono;
//
///**
// * 基础的异常全局处理
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
//@ControllerAdvice(basePackages = "club.p6e.coat.file")
//public class ExceptionRouterFunction {
//
//    /**
//     * 注入日志对象
//     */
//    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionRouterFunction.class);
//
//    @ResponseBody
//    @SuppressWarnings("all")
//    @ExceptionHandler(value = Exception.class)
//    public Object errorHandler(Exception exception) {
//        LOGGER.error(exception.getMessage());
//        exception.printStackTrace();
//        exception = CustomException.transformation(exception);
//        System.out.println("321321321");
//        if (exception instanceof final CustomException ce) {
//            return Mono.just(AspectHandlerFunction.ResultContext.build(ce.getCode(), ce.getSketch(), ce.getContent()));
//        }
//        return Mono.just(AspectHandlerFunction.ResultContext.build(500, "SERVICE_EXCEPTION", ""));
//    }
//}
