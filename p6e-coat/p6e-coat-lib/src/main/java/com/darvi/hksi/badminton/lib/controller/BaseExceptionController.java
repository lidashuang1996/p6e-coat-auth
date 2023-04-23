package com.darvi.hksi.badminton.lib.controller;

import com.darvi.hksi.badminton.lib.error.CustomException;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import reactor.core.publisher.Mono;

/**
 * 基础的异常全局处理
 *
 * @author lidashuang
 * @version 1.0
 */
@ControllerAdvice
public class BaseExceptionController {

    /**
     * 注入日志对象
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(BaseExceptionController.class);

    @ResponseBody
    @SuppressWarnings("all")
    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(Exception exception) {
        LOGGER.error(exception.getMessage());
        exception = CustomException.transformation(exception);
        if (exception instanceof CustomException) {
            final CustomException customException = (CustomException) exception;
            return this.isServletRequest()
                    ? ResultContext.build(customException.getCode(), customException.getSketch(), "")
                    : Mono.just(ResultContext.build(customException.getCode(), customException.getSketch(), ""));
        }
        exception.printStackTrace();
        return this.isServletRequest()
                ? ResultContext.build(500, "SERVICE_EXCEPTION", "")
                : Mono.just(ResultContext.build(500, "SERVICE_EXCEPTION", ""));
    }

    /**
     * 是否包含 javax.servlet.ServletRequest 对象
     *
     * @return 结果
     */
    private boolean isServletRequest() {
        boolean bool = true;
        try {
            Class.forName("jakarta.servlet.ServletRequest");
        } catch (ClassNotFoundException e) {
            bool = false;
        }
        return bool;
    }
}
