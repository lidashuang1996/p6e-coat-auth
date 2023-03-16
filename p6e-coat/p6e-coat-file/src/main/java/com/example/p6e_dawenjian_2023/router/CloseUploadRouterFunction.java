package com.example.p6e_dawenjian_2023.router;

import com.example.p6e_dawenjian_2023.handler.CloseUploadHandlerFunction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 关闭上传操作路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = CloseUploadRouterFunction.class,
        ignored = CloseUploadRouterFunction.class
)
public class CloseUploadRouterFunction extends BaseRouterFunction implements RouterFunction<ServerResponse> {

    public CloseUploadRouterFunction(CloseUploadHandlerFunction handlerFunction) {
        super(RequestPredicates.path("/upload/close")
                .or(RequestPredicates.DELETE("/upload/close"))
                .or(RequestPredicates.POST("/upload/close/{id}"))
                .or(RequestPredicates.DELETE("/upload/close/{id}")), handlerFunction);
    }

}