package club.p6e.coat.file.service.impl;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.model.UploadChunkModel;
import club.p6e.coat.file.repository.UploadChunkRepository;
import club.p6e.coat.file.repository.UploadRepository;
import club.p6e.coat.file.service.SliceUploadService;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.context.SliceUploadContext;
import club.p6e.coat.file.utils.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * 分片上传服务
 * 步骤2: 分片上传操作
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = SliceUploadService.class,
        ignored = SliceUploadServiceImpl.class
)
public class SliceUploadServiceImpl implements SliceUploadService {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 上传存储库对象
     */
    private final UploadRepository uploadRepository;

    /**
     * 上传块存储库对象
     */
    private final UploadChunkRepository uploadChunkRepository;

    /**
     * 构造方法初始化
     *
     * @param properties            配置文件对象
     * @param uploadRepository      上传存储库对象
     * @param uploadChunkRepository 上传块存储库对象
     */
    public SliceUploadServiceImpl(Properties properties,
                                  UploadRepository uploadRepository,
                                  UploadChunkRepository uploadChunkRepository) {
        this.properties = properties;
        this.uploadRepository = uploadRepository;
        this.uploadChunkRepository = uploadChunkRepository;
    }

    @Override
    public Mono<Map<String, Object>> execute(SliceUploadContext context) {
        final Integer id = context.getId();
        final String signature = context.getSignature();
        // 读取并清除文件对象
        final FilePart filePart = context.getFilePart();
        context.setFilePart(null);
        return uploadRepository
                .findById(id)
                .flatMap(m -> Mono.just(m)
                        .flatMap(um -> {
                            final UploadRepository repository = SpringUtil.getBean(UploadRepository.class);
                            // 文件绝对路径
                            final String absolutePath = FileUtil.convertAbsolutePath(FileUtil.composePath(
                                    properties.getSliceUpload().getPath(), um.getStorageLocation()));
                            final File absolutePathFile = new File(FileUtil.composePath(absolutePath, FileUtil.generateName()));
                            return repository
                                    // 获取锁
                                    .acquireLock(um.getId())
                                    // 写入文件数据
                                    .flatMap(file -> filePart.transferTo(absolutePathFile).then(Mono.just(absolutePathFile)))
                                    // 释放锁
                                    .flatMap(file -> repository.releaseLock(um.getId()))
                                    // 转换为文件对象输出
                                    .map(c -> absolutePathFile);
                        })
                        // 验证文件数据
                        .flatMap(file -> {
                            final long size = properties.getSliceUpload().getMaxSize();
                            return checkSize(file, size)
                                    .flatMap(v -> checkSignature(file, signature))
                                    .map(v -> file);
                        })
                        .flatMap(file -> {
                            final UploadChunkModel model = new UploadChunkModel();
                            model.setFid(m.getId());
                            model.setName(file.getName());
                            model.setSize(Long.valueOf(file.length()).intValue());
                            final Object operator = context.get("operator");
                            if (operator == null) {
                                if (m.getOperator() != null) {
                                    model.setOperator(m.getOperator());
                                }
                            } else {
                                model.setOperator(String.valueOf(operator));
                            }
                            return uploadChunkRepository.create(model);
                        })
                )
                .map(UploadChunkModel::toMap);
    }

    private static Mono<Void> checkSize(File file, long size) {
        if (file.length() > size) {
            FileUtil.deleteFile(file);
            throw new RuntimeException();
        }
        return Mono.never();
    }

    private static Mono<Void> checkSignature(File file, String signature) {
        return FileUtil
                .obtainMD5Signature(file)
                .flatMap(s -> {
                    if (!s.equals(signature)) {
                        FileUtil.deleteFile(file);
                        throw new RuntimeException();
                    }
                    return Mono.never();
                });
    }
}
