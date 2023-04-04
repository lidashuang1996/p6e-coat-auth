package club.p6e.coat.file;

import club.p6e.coat.file.file.UploadFileCleanStrategyService;
import club.p6e.coat.file.file.UploadFileCleanTask;
import club.p6e.coat.file.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class FileApplicationListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileApplicationListener.class);

    /**
     * Spring Boot 初始化完成事件监听
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // 初始化上下文对象
        SpringUtil.init(event.getApplicationContext());
        try {
            SpringUtil.getBean(FileBannerService.class).execute();
        } catch (Exception e) {
            // 忽略异常
        }
        // 打印配置文件的信息
        final Properties properties = SpringUtil.getBean(Properties.class);
        LOGGER.info("--------- " + properties.getClass() + " ---------");
        final Map<String, Properties.Resource> resources = properties.getResources();
        LOGGER.info("resources:");
        for (final String key : resources.keySet()) {
            LOGGER.info("     " + key + ":");
            final Properties.Resource resource = resources.get(key);
            LOGGER.info("          path:" + resource.getPath());
            LOGGER.info("          suffixes:" + resource.getSuffixes());
        }
        final Map<String, Properties.Download> downloads = properties.getDownloads();
        LOGGER.info("downloads:");
        for (final String key : downloads.keySet()) {
            LOGGER.info("     " + key + ":");
            final Properties.Download download = downloads.get(key);
            LOGGER.info("          path:" + download.getPath());
        }
        final Properties.SimpleUpload simpleUpload = properties.getSimpleUpload();
        LOGGER.info("simpleUpload:");
        LOGGER.info("     path:" + simpleUpload.getPath());
        LOGGER.info("     maxSize:" + simpleUpload.getMaxSize());
        final Properties.SliceUpload sliceUpload = properties.getSliceUpload();
        LOGGER.info("sliceUpload:");
        LOGGER.info("     path:" + sliceUpload.getPath());
        LOGGER.info("     maxSize:" + sliceUpload.getMaxSize());
        LOGGER.info("--------- " + properties.getClass() + " ---------");
        try {
            SpringUtil.getBean(UploadFileCleanTask.class);
            final UploadFileCleanStrategyService strategy = SpringUtil.getBean(UploadFileCleanStrategyService.class);
            LOGGER.info("File cleaning task enabled.");
            LOGGER.info("File cleaning task strategy is [ " + strategy.getClass() + " ].");
        } catch (Exception e) {
            LOGGER.info("The file cleaning task is not enabled.");
            LOGGER.warn("If you need to start it, please inject [ "
                    + UploadFileCleanTask.class + " ] into the Spring Bean factory.");
        }
        LOGGER.info("Initialization completed.");
    }
}
