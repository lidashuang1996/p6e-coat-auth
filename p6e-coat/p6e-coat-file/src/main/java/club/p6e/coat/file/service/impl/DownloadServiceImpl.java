package club.p6e.coat.file.service.impl;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.error.DownloadNodeException;
import club.p6e.coat.file.service.DownloadService;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.context.DownloadContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * 下载服务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = DownloadService.class,
        ignored = DownloadServiceImpl.class
)
public class DownloadServiceImpl implements DownloadService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public DownloadServiceImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Map<String, Object>> execute(DownloadContext context) {
        final Properties.Download download = properties.getDownloads().get(context.getNode());
        if (download == null) {
            throw new DownloadNodeException(this.getClass(),
                    "fun execute(DownloadContext context). node => value is null");
        } else {
            final Map<String, Object> result = new HashMap<>(3);
            result.put("node", context.getNode());
            result.put("path", context.getPath());
            result.put("__path__", FileUtil.convertAbsolutePath(
                    FileUtil.composePath(download.getPath(), context.getPath())
            ));
            return Mono.just(result);
        }
    }
}
