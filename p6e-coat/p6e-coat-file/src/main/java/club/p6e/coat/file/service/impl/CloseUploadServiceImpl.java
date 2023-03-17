package club.p6e.coat.file.service.impl;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.repository.UploadRepository;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.context.CloseUploadContext;
import club.p6e.coat.file.service.CloseUploadService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

/**
 * 分片上传服务
 * 步骤3: 关闭上传操作
 *
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
                    final String absolutePath = FileUtil.convertAbsolutePath(
                            FileUtil.composePath(properties.getPath(), m.getStorageLocation()));
                    final File[] files = FileUtil.readFolder(absolutePath);
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
