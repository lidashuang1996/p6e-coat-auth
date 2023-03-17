package club.p6e.coat.file.service.impl;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.service.DownloadService;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.context.DownloadContext;
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
public class DownloadServiceImpl implements DownloadService {

    private final Properties properties;

    public DownloadServiceImpl(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Mono<Map<String, Object>> execute(DownloadContext context) {
        final Properties.Download download = properties.getDownloads().get(context.getNode());
        if (download == null) {
            throw new RuntimeException();
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
