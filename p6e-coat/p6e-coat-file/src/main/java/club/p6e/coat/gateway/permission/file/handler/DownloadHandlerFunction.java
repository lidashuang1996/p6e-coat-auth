package club.p6e.coat.gateway.permission.file.handler;

import club.p6e.coat.gateway.permission.file.aspect.DownloadAspect;
import club.p6e.coat.gateway.permission.file.error.FileException;
import club.p6e.coat.gateway.permission.file.mapper.RequestParameterMapper;
import club.p6e.coat.gateway.permission.file.service.DownloadService;
import club.p6e.coat.gateway.permission.file.utils.FileUtil;
import club.p6e.coat.gateway.permission.file.context.DownloadContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 下载操作处理程序函数
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = DownloadHandlerFunction.class,
        ignored = DownloadHandlerFunction.class
)
public class DownloadHandlerFunction extends AspectHandlerFunction implements HandlerFunction<ServerResponse> {

    /**
     * 下载切面对象
     */
    private final DownloadAspect aspect;

    /**
     * 下载服务对象
     */
    private final DownloadService service;

    /**
     * 构造函数初始化
     *
     * @param aspect  下载切面对象
     * @param service 下载服务对象
     */
    public DownloadHandlerFunction(DownloadAspect aspect, DownloadService service) {
        this.aspect = aspect;
        this.service = service;
    }

    @NonNull
    @Override
    public Mono<ServerResponse> handle(@NonNull ServerRequest request) {
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
                        .flatMap(r -> {
                            // 读取下载文件的绝对路径
                            final Object downloadPath = r.get("__path__");
                            if (downloadPath == null) {
                                // 如果不存在下载文件路径数据则抛出异常
                                return Mono.error(new FileException(
                                        this.getClass(),
                                        "fun handle(ServerRequest request). -> Download file path is null.",
                                        "Download file path is null"
                                ));
                            } else if (downloadPath instanceof final String dps) {
                                final File file = new File(dps);
                                // 验证文件是否存在
                                if (FileUtil.checkFileExist(file)) {
                                    return Mono.just(file);
                                } else {
                                    // 文件不存在抛出异常
                                    return Mono.error(new FileException(
                                            this.getClass(),
                                            "fun handle(ServerRequest request). -> Download file not exist.",
                                            "Download file not exist"
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
                        .flatMap(f -> {
                            String fc = "unknown";
                            final String fn = FileUtil.name(f.getName());
                            try {
                                if (fn != null) {
                                    fc = URLEncoder.encode(fn, StandardCharsets.UTF_8);
                                }
                            } catch (Exception e) {
                                // 忽略异常
                                return Mono.error(new FileException(
                                        this.getClass(),
                                        "fun handle(ServerRequest request). -> " +
                                                "Download file name parsing error.",
                                        "Download file name parsing error"
                                ));
                            }
                            final ServerResponse.BodyBuilder builder =
                                    ServerResponse.ok()
                                            .header("Content-Disposition",
                                                    "attachment; filename=" + fc)
                                            .contentType(MediaType.APPLICATION_OCTET_STREAM);
                            return builder.body((response, context) -> response.writeWith(FileUtil.readFile(f)));
                        });
    }

}
