package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.repository.PermissionRepository;
import club.p6e.coat.gateway.permission.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 权限任务
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionTask.class,
        ignored = PermissionTask.class
)
public final class PermissionTask {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionTask.class);

    /**
     * 配置文件对象
     */
    private final long interval;

    /**
     * 上次更新的时间
     */
    private LocalDateTime lastUpdateDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

    /**
     * 构造方法初始化
     */
    public PermissionTask(Properties properties) {
        this.interval = properties.getTask().getInterval()
                + ThreadLocalRandom.current().nextInt(900_000);
    }

    @Scheduled(initialDelay = 2_000, fixedDelay = 60_000)
    public void execute() {
        final LocalDateTime now = LocalDateTime.now();
        LOGGER.info("[TASK] Start updating data.");
        LOGGER.info("[TASK] ==> NOW: " + now + " , LAST_UPDATE_TIME: " + lastUpdateDateTime);
        if (Duration.between(lastUpdateDateTime, now).toMillis() > interval) {
            execute0().subscribe();
            lastUpdateDateTime = LocalDateTime.now();
            LOGGER.info("[TASK] refresh LAST_UPDATE_TIME: " + lastUpdateDateTime);
        } else {
            LOGGER.info("[TASK] Not exceeding the interval time, task closed !!");
        }
        LOGGER.info("[TASK] End updating data.");
    }

    /**
     * 执行数据读取并写入到缓存对象中
     *
     * @return Mono/Boolean 是否读取写入成功
     */
    private Mono<Boolean> execute0() {
        final int page = 1;
        final int size = 20;
        final List<PermissionDetails> list = new ArrayList<>();
        return execute1(page, size, list)
                .map(b -> {
                    if (b) {
                        LOGGER.info("[TASK] Successfully read data [" + list.size() + "].");
                        PermissionCore.cache(list);
                    }
                    return b;
                });
    }

    /**
     * 执行数据按照页码/页长开始读取直到数据读取完成
     *
     * @param page 页码
     * @param size 页长
     * @param list 已经读取的数据
     * @return Mono/Boolean 是否读取成功
     */
    private Mono<Boolean> execute1(int page, int size, List<PermissionDetails> list) {
        return execute2(page, size, list)
                .map(c -> c == size)
                .flatMap(b -> b ? execute1(page + 1, size, list) : Mono.just(true))
                .onErrorResume(t -> Mono.just(false));
    }

    /**
     * 执行数据按照页码/页长读取
     *
     * @param page 页码
     * @param size 页长
     * @param list 已经读取的数据
     * @return Mono/Integer 本次读取的数据长度
     */
    private Mono<Integer> execute2(int page, int size, List<PermissionDetails> list) {
        LOGGER.info("[TASK] read data page: " + page + ", size:  " + size + " ::: [" + list.size() + "].");
        final PermissionRepository repository = SpringUtil.getBean(PermissionRepository.class);
        return repository
                .findByPermissionUrlTableAll(page, size)
                .collectList()
                .map(l -> {
                    list.addAll(l);
                    return l.size();
                })
                .onErrorResume(throwable -> Mono.just(0));
    }

}
