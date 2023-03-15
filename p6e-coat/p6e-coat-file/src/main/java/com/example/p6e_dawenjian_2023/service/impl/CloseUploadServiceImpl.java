package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.context.CloseUploadContext;
import com.example.p6e_dawenjian_2023.model.UploadModel;
import com.example.p6e_dawenjian_2023.repository.UploadRepository;
import com.example.p6e_dawenjian_2023.service.CloseUploadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class CloseUploadServiceImpl implements CloseUploadService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 上传存储库对象
     */
    private final UploadRepository repository;

    /**
     * 构造方法初始化
     *
     * @param repository 上传存储库对象
     */
    public CloseUploadServiceImpl(Properties properties, UploadRepository repository) {
        this.properties = properties;
        this.repository = repository;
    }


    @Override
    public Mono<Map<String, Object>> execute(CloseUploadContext context) {
        return repository
                .closeLock(context.getId())
                .map(m -> {
                    final String a = properties.getPath() + m.getStorageLocation();
                    final File[] files = FileUtil.readFolder(a);
                    for (int i = 0; i < files.length; i++) {
                        for (int j = i; j < files.length; j++) {
                            final String in = files[i].getName();
                            final String jn = files[j].getName();
                            final int iw = Integer.parseInt(in.substring(0, in.indexOf("_")));
                            final int jw = Integer.parseInt(jn.substring(0, jn.indexOf("_")));
                            if (iw > jw) {
                                final File v = files[j];
                                files[j] = files[i];
                                files[i] = v;
                            }
                        }
                    }
                    final Map<String, Object> map = m.toMap();
                    map.put("files", files);
                    return map;
                });
    }

}
