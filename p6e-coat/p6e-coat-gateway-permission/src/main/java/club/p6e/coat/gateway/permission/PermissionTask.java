package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.model.PermissionModel;
import club.p6e.coat.gateway.permission.repository.PermissionRepository;
import club.p6e.coat.gateway.permission.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限任务
 *
 * @author lidashuang
 * @version 1.0
 */
public final class PermissionTask {

    /**
     * 任务间隔
     */
    private static final int INTERVAL = 3 * 3600 * 1000;

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionTask.class);

    /**
     * 上次更新的时间
     */
    private static LocalDateTime LAST_UPDATE_TIME = LocalDateTime.MIN;

    public static void execute() {
        final LocalDateTime now = LocalDateTime.now();
        LOGGER.info("[TASK] Start updating data.");
        LOGGER.info("[TASK] ==> NOW: " + now + " , LAST_UPDATE_TIME: " + LAST_UPDATE_TIME);
        if (Duration.between(LAST_UPDATE_TIME, now).toMillis() > INTERVAL) {
            execute0().subscribe();
            LAST_UPDATE_TIME = LocalDateTime.now();
            LOGGER.info("[TASK] refresh LAST_UPDATE_TIME: " + LAST_UPDATE_TIME);
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
    private static Mono<Boolean> execute0() {
        final int page = 1;
        final int size = 20;
        final List<PermissionModel> list = new ArrayList<>();
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
    private static Mono<Boolean> execute1(int page, int size, List<PermissionModel> list) {
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
    private static Mono<Integer> execute2(int page, int size, List<PermissionModel> list) {
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
