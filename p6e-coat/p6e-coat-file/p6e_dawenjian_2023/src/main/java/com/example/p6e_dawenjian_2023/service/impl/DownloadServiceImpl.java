package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.context.DownloadContext;
import com.example.p6e_dawenjian_2023.service.DownloadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
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
