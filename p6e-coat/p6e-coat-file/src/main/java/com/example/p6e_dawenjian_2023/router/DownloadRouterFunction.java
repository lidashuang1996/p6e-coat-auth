package com.example.p6e_dawenjian_2023.router;

import com.example.p6e_dawenjian_2023.handler.DownloadHandlerFunction;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 下载操作路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(DownloadRouterFunction.class)
public class DownloadRouterFunction extends BaseRouterFunction implements RouterFunction<ServerResponse> {

    public DownloadRouterFunction(DownloadHandlerFunction handlerFunction) {
        super(RequestPredicates.GET("/download"), handlerFunction);
    }

}