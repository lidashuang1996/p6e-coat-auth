package com.example.p6e_dawenjian_2023.handler;

import com.example.p6e_dawenjian_2023.aspect.OpenUploadAspect;
import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import com.example.p6e_dawenjian_2023.mapper.RequestParameterMapper;
import com.example.p6e_dawenjian_2023.service.OpenUploadService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 打开上传操作处理程序函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class CloseUploadHandlerFunction extends AspectHandlerFunction implements HandlerFunction<ServerResponse> {

    /**
     * 打开上传切面对象
     */
    private final OpenUploadAspect aspect;

    /**
     * 打开上传服务对象
     */
    private final OpenUploadService service;

    /**
     * 构造函数初始化
     *
     * @param aspect  打开上传切面对象
     * @param service 打开上传服务对象
     */
    public CloseUploadHandlerFunction(OpenUploadAspect aspect, OpenUploadService service) {
        this.aspect = aspect;
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return
                // 通过请求参数映射器获取上下文对象
                RequestParameterMapper.execute(request, OpenUploadContext.class)
                        // 执行打开上传操作之前的切点
                        .flatMap(c -> before(aspect, c.toMap()))
                        .flatMap(m -> {
                            final OpenUploadContext context = new OpenUploadContext(m);
                            return
                                    // 执行打开上传服务
                                    service.execute(context)
                                            // 执行打开上传操作之后的切点
                                            .flatMap(r -> after(aspect, context.toMap(), r));
                        })
                        // 结果返回
                        .flatMap(r -> ServerResponse.ok().bodyValue(r));
    }

}
