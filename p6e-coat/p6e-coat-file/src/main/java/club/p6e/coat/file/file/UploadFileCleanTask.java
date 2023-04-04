package club.p6e.coat.file.file;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 文件清除任务
 *
 * @author lidashuang
 * @version 1.0
 */
@EnableScheduling
@EnableTransactionManagement
public class UploadFileCleanTask {

    /**
     * 文件清除策略对象
     */
    private final UploadFileCleanStrategy strategy;

    /**
     * 构造方法初始化
     *
     * @param strategy 文件清除策略对象
     */
    public UploadFileCleanTask(UploadFileCleanStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * 定时任务执行
     * 定时任务是初始化 5S 执行一次后，每次间隔 10 分钟执行一次
     */
    @Scheduled(initialDelay = 5_000, fixedDelay = 10 * 60_000)
    public void execute() {
        try {
            this.strategy.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
