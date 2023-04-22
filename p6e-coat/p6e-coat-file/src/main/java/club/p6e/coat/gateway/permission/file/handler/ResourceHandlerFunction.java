package club.p6e.coat.gateway.permission.file.handler;

import club.p6e.coat.gateway.permission.file.aspect.ResourceAspect;
import club.p6e.coat.gateway.permission.file.context.ResourceContext;
import club.p6e.coat.gateway.permission.file.error.FileException;
import club.p6e.coat.gateway.permission.file.error.MediaTypeException;
import club.p6e.coat.gateway.permission.file.mapper.RequestParameterMapper;
import club.p6e.coat.gateway.permission.file.service.ResourceService;
import club.p6e.coat.gateway.permission.file.utils.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.Serializable;

/**
 * 资源操作处理程序函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = ResourceHandlerFunction.class,
        ignored = ResourceHandlerFunction.class
)
public class ResourceHandlerFunction extends AspectHandlerFunction implements HandlerFunction<ServerResponse> {

    /**
     * 下载切面对象
     */
    private final ResourceAspect aspect;

    /**
     * 下载服务对象
     */
    private final ResourceService service;

    /**
     * 构造函数初始化
     *
     * @param aspect  下载切面对象
     * @param service 下载服务对象
     */
    public ResourceHandlerFunction(ResourceAspect aspect, ResourceService service) {
        this.aspect = aspect;
        this.service = service;
    }

    @NonNull
    @Override
    public Mono<ServerResponse> handle(@NonNull ServerRequest request) {
        return
                // 通过请求参数映射器获取上下文对象
                RequestParameterMapper.execute(request, ResourceContext.class)
                        // 执行下载操作之前的切点
                        .flatMap(c -> before(aspect, c.toMap()))
                        .flatMap(m -> {
                            final ResourceContext context = new ResourceContext(m);
                            return
                                    // 执行下载服务
                                    service.execute(context)
                                            // 执行下载操作之后的切点
                                            .flatMap(r -> after(aspect, context.toMap(), r));
                        })
                        // 获取返回结果中的文件路径
                        // 并将返回的文件路径转换为文件对象
                        .flatMap(r -> {
                            // 读取下载文件的绝对路径
                            final Object resourcePath = r.get("__path__");
                            final Object mediaType = r.get("__media_type__");
                            if (resourcePath == null) {
                                // 如果不存在下载文件路径数据则抛出异常
                                return Mono.error(new FileException(
                                        this.getClass(),
                                        "fun handle(ServerRequest request). -> Resource file path is null.",
                                        "Resource file path is null"
                                ));
                            } else if (resourcePath instanceof final String dps) {
                                final File file = new File(dps);
                                // 验证文件是否存在
                                if (FileUtil.checkFileExist(file)) {
                                    if (mediaType instanceof final MediaType my) {
                                        return Mono.just(new ResourceModel(file, my));
                                    } else {
                                        return Mono.error(new MediaTypeException(
                                                this.getClass(),
                                                "fun handle(ServerRequest request). -> Resource file media type error.",
                                                "Resource file media type error"
                                        ));
                                    }
                                } else {
                                    // 文件不存在抛出异常
                                    return Mono.error(new FileException(
                                            this.getClass(),
                                            "fun handle(ServerRequest request). -> Resource file not exist.",
                                            "Resource file not exist"
                                    ));
                                }
                            } else {
                                // 如果为其他类型的数据则抛出异常
                                return Mono.error(new FileException(
                                        this.getClass(),
                                        "fun handle(ServerRequest request). -> " +
                                                "Download file path data type not is String.",
                                        "Download file path data type not is String"
                                ));
                            }
                        })
                        // 结果返回
                        .flatMap(m -> ServerResponse
                                .ok()
                                .contentType(m.mediaType())
                                .body((response, context) -> response.writeWith(FileUtil.readFile(m.file())))
                        );
    }

    /**
     * 资源模型
     *
     * @param file      文件对象
     * @param mediaType 媒体类型
     */
    private record ResourceModel(File file, MediaType mediaType) implements Serializable {
    }

}
