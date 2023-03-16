package com.example.p6e_dawenjian_2023.router;

import com.example.p6e_dawenjian_2023.handler.SimpleUploadHandlerFunction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 简单（小文件）上传操作路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(SimpleUploadRouterFunction.class)
public class SimpleUploadRouterFunction extends BaseRouterFunction implements RouterFunction<ServerResponse> {

    public SimpleUploadRouterFunction(SimpleUploadHandlerFunction handlerFunction) {
        super(RequestPredicates.POST("/upload/simple"), handlerFunction);
    }

}
