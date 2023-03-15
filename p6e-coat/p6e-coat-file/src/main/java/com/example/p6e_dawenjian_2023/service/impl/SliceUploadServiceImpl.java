package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.context.SliceUploadContext;
import com.example.p6e_dawenjian_2023.model.UploadChunkModel;
import com.example.p6e_dawenjian_2023.repository.UploadChunkRepository;
import com.example.p6e_dawenjian_2023.repository.UploadRepository;
import com.example.p6e_dawenjian_2023.service.SliceUploadService;
import com.example.p6e_dawenjian_2023.utils.FileUtil;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class SliceUploadServiceImpl implements SliceUploadService {


    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 上传存储库对象
     */
    private final UploadRepository uploadRepository;
    private final UploadChunkRepository uploadChunkRepository;


    public SliceUploadServiceImpl(Properties properties, UploadRepository uploadRepository, UploadChunkRepository uploadChunkRepository) {
        this.properties = properties;
        this.uploadRepository = uploadRepository;
        this.uploadChunkRepository = uploadChunkRepository;
    }

    @Override
    public Mono<Map<String, Object>> execute(SliceUploadContext context) {
        final Integer id = context.getId();
        final String signature = context.getSignature();
        final FilePart filePart = context.getFilePart();
        // 读取完成就删除对文件的引用
        context.setFilePart(null);
        return uploadRepository
                .findById(id)
                .flatMap(m -> Mono.just(m)
                        .map(um -> {
                            // 文件绝对路径
                            final String absolutePath = FileUtil.convertAbsolutePath(
                                    FileUtil.composePath(properties.getPath(), um.getStorageLocation()));
                            // 文件对象
                            return new File(FileUtil.composePath(absolutePath, FileUtil.generateName()));
                        })
                        // 写入文件数据
                        .flatMap(file -> filePart.transferTo(file).then(Mono.just(file)))
                        // 验证文件数据
                        .flatMap(file -> {
                            // 验证文件长度
                            checkSize(file, properties.getMaxSize());
                            // 验证文件签名
                            return checkSignature(file, signature);
                        })
                        .flatMap(file -> {
                            final UploadChunkModel model = new UploadChunkModel();
                            model.setName(file.getName());
                            model.setFid(m.getId());
                            model.setAction("xxxx");
                            return uploadChunkRepository.create(model);
                        })
                )
                .map(UploadChunkModel::toMap);
    }

    private static void checkSize(File file, long size) {
        if (file.length() > size) {
            FileUtil.deleteFile(file);
            throw new RuntimeException();
        }
    }

    private static Mono<File> checkSignature(File file, String signature) {
        return FileUtil
                .obtainMD5Signature(file)
                .map(s -> {
                    System.out.println(s);
                    if (s.equals(signature)) {
                        return file;
                    } else {
                        FileUtil.deleteFile(file);
                        throw new RuntimeException();
                    }
                });
    }
}
