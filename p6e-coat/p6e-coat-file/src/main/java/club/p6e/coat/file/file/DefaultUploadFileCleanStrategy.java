package club.p6e.coat.file.file;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.utils.SpringUtil;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DefaultUploadFileCleanStrategy implements UploadFileCleanStrategy {

    @Override
    public void execute() {
        final Properties properties = SpringUtil.getBean(Properties.class);
        final Properties.SliceUpload sliceUpload = properties.getSliceUpload();
        final Properties.SimpleUpload simpleUpload = properties.getSimpleUpload();
        // 清除磁盘文件


        // 清除数据库的缓存数据
        
    }

}
