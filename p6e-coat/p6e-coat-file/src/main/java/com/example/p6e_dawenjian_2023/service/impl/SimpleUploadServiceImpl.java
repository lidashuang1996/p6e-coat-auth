package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.context.SimpleUploadContext;
import com.example.p6e_dawenjian_2023.folder.UploadFolderStorageLocationPathService;
import com.example.p6e_dawenjian_2023.model.UploadModel;
import com.example.p6e_dawenjian_2023.repository.UploadRepository;
import com.example.p6e_dawenjian_2023.service.SimpleUploadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
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
public class SimpleUploadServiceImpl implements SimpleUploadService {

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

    public SimpleUploadServiceImpl(Properties properties, UploadRepository repository,
                                   UploadFolderStorageLocationPathService folderPathService) {
        this.properties = properties;
        this.repository = repository;
        this.folderPathService = folderPathService;
    }

    @Override
    public Mono<Map<String, Object>> execute(SimpleUploadContext context) {
        final FilePart filePart = context.getFilePart();
        context.setFilePart(null);
        final UploadModel model = new UploadModel();
        final String path = folderPathService.path();
        final String absolutePath = FileUtil.convertAbsolutePath(FileUtil.composePath(properties.getPath(), path));
        model.setName(filePart.filename());
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
                            .then(Mono.just(file.length()))
                            .map(len -> {
                                if (len > properties.getSimpleUpload().getMaxSize()) {
                                    throw new RuntimeException();
                                } else {
                                    return m;
                                }
                            });
                })
                .map(UploadModel::toMap);
    }

}
