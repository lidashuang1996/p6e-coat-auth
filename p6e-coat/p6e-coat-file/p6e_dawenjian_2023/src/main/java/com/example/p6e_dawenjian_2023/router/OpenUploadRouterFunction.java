package com.example.p6e_dawenjian_2023.router;

import com.example.p6e_dawenjian_2023.handler.OpenUploadHandlerFunction;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 打开上传操作路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class OpenUploadRouterFunction extends BaseRouterFunction implements RouterFunction<ServerResponse> {

    public OpenUploadRouterFunction(OpenUploadHandlerFunction handlerFunction) {
        super(RequestPredicates.POST("/upload/open"), handlerFunction);
    }

}