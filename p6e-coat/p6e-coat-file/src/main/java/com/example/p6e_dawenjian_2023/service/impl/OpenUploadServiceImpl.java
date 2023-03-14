package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.folder.UploadFolderStorageLocationPathService;
import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import com.example.p6e_dawenjian_2023.model.UploadModel;
import com.example.p6e_dawenjian_2023.repository.UploadRepository;
import com.example.p6e_dawenjian_2023.service.OpenUploadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 分片上传
 * 步骤1: 打开上传操作
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class OpenUploadServiceImpl implements OpenUploadService {

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
    public OpenUploadServiceImpl(Properties properties,
                                 UploadRepository repository,
                                 UploadFolderStorageLocationPathService folderPathService) {
        this.properties = properties;
        this.repository = repository;
        this.folderPathService = folderPathService;
    }

    @Override
    public Mono<Map<String, Object>> execute(OpenUploadContext context) {
        final UploadModel model = new UploadModel();
        final String path = folderPathService.path();
        final String absolutePath = FileUtil.convertAbsolutePath(FileUtil.composePath(properties.getPath(), path));
        model.setName(context.getName());
        model.setStorageLocation(path);
        return repository
                .create(model)
                .map(m -> {
                    // 如果创建数据成功就创建文件夹
                    FileUtil.createFolder(absolutePath);
                    return m;
                })
                .map(UploadModel::toMap);
    }

}
