package club.p6e.coat.file.service.impl;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.context.SimpleUploadContext;
import club.p6e.coat.file.folder.UploadFolderStorageLocationPathService;
import club.p6e.coat.file.model.UploadModel;
import club.p6e.coat.file.service.SimpleUploadService;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.repository.UploadRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

/**
 * 简单（小文件）上传服务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = SimpleUploadService.class,
        ignored = SimpleUploadServiceImpl.class
)
public class SimpleUploadServiceImpl implements SimpleUploadService {

    /**
     * 源
     */
    private static final String SOURCE = "SIMPLE_UPLOAD";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 上传存储库对象
     */
    private final UploadRepository repository;

    /**
     * 上传文件夹本地存储路径服务对象
     */
    private final UploadFolderStorageLocationPathService folderPathService;

    /**
     * 构造方法初始化
     *
     * @param properties        配置文件对象
     * @param repository        上传存储库对象
     * @param folderPathService 上传文件夹本地存储路径服务对象
     */
    public SimpleUploadServiceImpl(Properties properties,
                                   UploadRepository repository,
                                   UploadFolderStorageLocationPathService folderPathService) {
        this.properties = properties;
        this.repository = repository;
        this.folderPathService = folderPathService;
    }

    @Override
    public Mono<Map<String, Object>> execute(SimpleUploadContext context) {
        // 读取并清除文件对象
        final FilePart filePart = context.getFilePart();
        context.setFilePart(null);
        final UploadModel model = new UploadModel();
        final String name = filePart.filename();
        final String path = folderPathService.path();
        final String absolutePath = FileUtil.convertAbsolutePath(
                FileUtil.composePath(properties.getPath(), path));
        final Object operator = context.get("operator");
        if (operator != null) {
            model.setOperator(String.valueOf(operator));
        }
        model.setName(name);
        model.setSource(SOURCE);
        model.setStorageLocation(path);
        return repository
                .create(model)
                .map(m -> {
                    // 如果创建数据成功就创建文件夹
                    FileUtil.createFolder(absolutePath);
                    return m;
                })
                .flatMap(m -> {
                    final File file = new File(FileUtil.composePath(absolutePath, filePart.filename()));
                    return filePart
                            .transferTo(file)
                            .then(Mono.just(file))
                            .flatMap(f -> {
                                final long size = properties.getSimpleUpload().getMaxSize();
                                return checkSize(f, size).map(v -> m);
                            });
                })
                .map(UploadModel::toMap);
    }

    private static Mono<Void> checkSize(File file, long size) {
        if (file.length() > size) {
            FileUtil.deleteFile(file);
            throw new RuntimeException();
        }
        return Mono.never();
    }

}
