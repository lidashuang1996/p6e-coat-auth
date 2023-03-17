package club.p6e.coat.file.folder;

import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.utils.GeneratorUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 默认实现上传文件的本地存储路径服务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = UploadFolderStorageLocationPathService.class,
        ignored = DefaultUploadFolderStorageLocationPathServiceImpl.class
)
public class DefaultUploadFolderStorageLocationPathServiceImpl implements UploadFolderStorageLocationPathService {

    /**
     * 时间格式化对象
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    @Override
    public String path() {
        // 文件路径由时间 + UUID 生成
        return FileUtil.composePath(DATE_TIME_FORMATTER.format(LocalDateTime.now()),
                GeneratorUtil.uuid() + GeneratorUtil.random(6, true, false));
    }

}
