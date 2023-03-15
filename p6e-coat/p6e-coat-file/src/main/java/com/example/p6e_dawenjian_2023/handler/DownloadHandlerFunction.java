package com.example.p6e_dawenjian_2023.handler;

import com.example.p6e_dawenjian_2023.aspect.DownloadAspect;
import com.example.p6e_dawenjian_2023.context.DownloadContext;
import com.example.p6e_dawenjian_2023.mapper.RequestParameterMapper;
import com.example.p6e_dawenjian_2023.service.DownloadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DownloadHandlerFunction extends AspectHandlerFunction implements HandlerFunction<ServerResponse> {

    /**
     * 打开上传切面对象
     */
    private final DownloadAspect aspect;

    /**
     * 打开上传服务对象
     */
    private final DownloadService service;

    public DownloadHandlerFunction(DownloadAspect aspect, DownloadService service) {
        this.aspect = aspect;
        this.service = service;
    }

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        return
                // 通过请求参数映射器获取上下文对象
                RequestParameterMapper.execute(request, DownloadContext.class)
                        // 执行下载操作之前的切点
                        .flatMap(c -> before(aspect, c.toMap()))
                        .flatMap(m -> {
                            final DownloadContext context = new DownloadContext(m);
                            return
                                    // 执行下载服务
                                    service.execute(context)
                                            // 执行下载操作之后的切点
                                            .flatMap(r -> after(aspect, context.toMap(), r));
                        })
                        // 获取返回结果中的文件路径
                        // 并将返回的文件路径转换为文件对象
                        .map(r -> {
                            // 读取下载文件的绝对路径
                            final Object downloadPath = r.get("__path__");
                            if (downloadPath == null) {
                                // 如果不存在下载文件的绝对路径则抛出异常
                                throw new RuntimeException();
                            } else if (downloadPath instanceof final String dps) {
                                final File file = new File(dps);
                                // 验证文件是否存在
                                if (!FileUtil.checkFileExist(file)) {
                                    // 文件不存在抛出异常
                                    throw new RuntimeException();
                                }
                                return file;
                            } else {
                                // 如果为其他类型的数据则抛出异常
                                throw new RuntimeException();
                            }
                        })
                        // 结果返回
                        .flatMap(f -> ServerResponse.ok().bodyValue(f));
    }

}
