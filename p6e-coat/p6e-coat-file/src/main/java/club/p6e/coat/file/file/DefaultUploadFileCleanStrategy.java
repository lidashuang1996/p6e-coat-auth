package club.p6e.coat.file.file;

import club.p6e.coat.file.Properties;
import club.p6e.coat.file.repository.UploadChunkRepository;
import club.p6e.coat.file.repository.UploadRepository;
import club.p6e.coat.file.utils.FileUtil;
import club.p6e.coat.file.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 默认实现上传文件清除策略
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DefaultUploadFileCleanStrategy implements UploadFileCleanStrategy {

    /**
     * 分片 数据源
     */
    private static final String SLICE_SOURCE = "SLICE_UPLOAD";

    /**
     * 简单（小文件） 数据源
     */
    private static final String SIMPLE_UPLOAD = "SIMPLE_UPLOAD";

    /**
     * 日志系统
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUploadFileCleanStrategy.class);

    /**
     * "{{ht}" 小时删除
     */
    private final int ht;

    /**
     * "{mt}x" 分钟删除
     */
    private final int mt;

    /**
     * 构造方法初始化
     */
    @SuppressWarnings("all")
    public DefaultUploadFileCleanStrategy() {
        this.ht = ThreadLocalRandom.current().nextInt(6);
        this.mt = ThreadLocalRandom.current().nextInt(6);
        LOGGER.info("[ FILE CLEAN ] INIT ==> HT: " + this.ht + " MT: "
                + this.mt + " >>> XXXX-XX-XX 0" + this.ht + ":" + this.mt + "X:XX");
    }

    @Override
    public void execute() {
        final Properties properties = SpringUtil.getBean(Properties.class);
        final Properties.SliceUpload sliceUpload = properties.getSliceUpload();
        final Properties.SimpleUpload simpleUpload = properties.getSimpleUpload();
        final UploadRepository uploadRepository = SpringUtil.getBean(UploadRepository.class);
        final UploadChunkRepository uploadChunkRepository = SpringUtil.getBean(UploadChunkRepository.class);
        // 查询需要处理的数据
        final AtomicInteger id = new AtomicInteger(0);
        final AtomicBoolean status = new AtomicBoolean(true);
        // 删除 7 天以前的数据
        final LocalDateTime localDateTime = LocalDateTime.now().minusDays(7);
        while (status.get()) {
            uploadRepository
                    .findByCreateDateOne(id.get(), null, localDateTime)
                    .map(m -> {
                        id.set(m.getId());
                        return m;
                    })
                    .switchIfEmpty(Mono
                            .just(false)
                            .flatMap(b -> {
                                status.set(b);
                                return Mono.empty();
                            }))
                    .flatMap(m -> uploadRepository
                            .update(m.setRubbish(1))
                            .flatMap(c -> {
                                if (c > 0) {
                                    // 清除磁盘文件
                                    if (SLICE_SOURCE.equals(m.getSource())) {
                                        FileUtil.deleteFolder(FileUtil.composePath(
                                                sliceUpload.getPath(), m.getStorageLocation()));
                                    }
                                    if (SIMPLE_UPLOAD.equals(m.getSource())) {
                                        FileUtil.deleteFolder(FileUtil.composePath(
                                                simpleUpload.getPath(), m.getStorageLocation()));
                                    }
                                    return Mono.just(m);
                                } else {
                                    return Mono.empty();
                                }
                            }))
                    .flatMap(m -> uploadRepository
                            // 清除数据库的缓存数据
                            .deleteById(m.getId())
                            .flatMap(c -> {
                                if (c > 0) {
                                    return uploadChunkRepository.deleteByFid(m.getId());
                                } else {
                                    return Mono.empty();
                                }
                            }))
                    .subscribe();
        }
    }

    @Override
    public boolean time() {
        final int hour = LocalDateTime.now().getHour();
        final int minute = LocalDateTime.now().getMinute();
        return this.ht == hour
                || this.mt == ((int) Math.floor(minute / 10F));
    }

}
