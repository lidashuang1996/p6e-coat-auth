package club.p6e.coat.file.handler;

import club.p6e.coat.file.aspect.ResourceAspect;
import club.p6e.coat.file.aspect.ResourceAspect;
import club.p6e.coat.file.context.ResourceContext;
import club.p6e.coat.file.error.FileException;
import club.p6e.coat.file.mapper.RequestParameterMapper;
import club.p6e.coat.file.service.ResourceService;
import club.p6e.coat.file.utils.FileUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;

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

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
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
                        .map(r -> {
                            // 读取下载文件的绝对路径
                            final Object ResourcePath = r.get("__path__");
                            if (ResourcePath == null) {
                                // 如果不存在下载文件路径数据则抛出异常
                                throw new FileException(this.getClass(),
                                        "fun handle(ServerRequest request).", "resource file path is null");
                            } else if (ResourcePath instanceof final String dps) {
                                final File file = new File(dps);
                                // 验证文件是否存在
                                if (FileUtil.checkFileExist(file)) {
                                    return file;
                                } else {
                                    // 文件不存在抛出异常
                                    throw new FileException(this.getClass(),
                                            "fun handle(ServerRequest request).", "resource file does not exist");
                                }
                            } else {
                                // 如果为其他类型的数据则抛出异常
                                throw new FileException(this.getClass(),
                                        "fun handle(ServerRequest request).",
                                        "resource file path data type not is <java.lang.String>");
                            }
                        })
                        // 结果返回
                        .flatMap(f -> request.exchange().getResponse().writeWith(FileUtil.readFile(f)))
                        .flatMap(v -> ServerResponse.ok().build());
    }

}
