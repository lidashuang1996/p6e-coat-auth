package com.example.p6e_dawenjian_2023.router;

import com.example.p6e_dawenjian_2023.handler.SliceUploadHandlerFunction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 分片上传操作路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(SliceUploadRouterFunction.class)
public class SliceUploadRouterFunction extends BaseRouterFunction implements RouterFunction<ServerResponse> {

    public SliceUploadRouterFunction(SliceUploadHandlerFunction handlerFunction) {
        super(RequestPredicates.POST("/upload/slice")
                .or(RequestPredicates.POST("/upload/slice/{id}")), handlerFunction);
    }

}