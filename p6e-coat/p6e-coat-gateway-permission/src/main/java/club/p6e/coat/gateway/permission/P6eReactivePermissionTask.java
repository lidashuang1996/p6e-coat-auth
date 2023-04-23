package club.p6e.coat.gateway.permission;

import club.p6e.coat.gateway.permission.cache.PermissionLockCache;
import club.p6e.coat.gateway.permission.model.PermissionModel;
import club.p6e.coat.gateway.permission.repository.PermissionRepository;
import club.p6e.coat.gateway.permission.utils.SpringUtil;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class P6eReactivePermissionTask {

    private static final int INTERVAL = 3 * 3600 * 1000;

    public static void execute() {
        final PermissionLockCache cache = SpringUtil.getBean(PermissionLockCache.class);
        cache.acquireLock()
                .flatMap(b -> {
                    if (b) {
                        final LocalDateTime now = LocalDateTime.now();
                        return cache
                                .getLastUpdateTime()
                                .map(t -> Duration.between(t, now).toMillis() > INTERVAL)
                                .flatMap(tb -> tb ? execute0() : Mono.just(false))
                                .flatMap(cb -> cb ? cache.setLastUpdateTime().map(l -> true) : Mono.just(false));
                    } else {
                        return Mono.just(false);
                    }
                })
                .publishOn(Schedulers.boundedElastic())
                .doFinally(st -> cache.releaseLock().subscribe())
                .subscribe();
    }

    private static Mono<Boolean> execute0() {
        final int page = 1;
        final int size = 20;
        final List<PermissionModel> list = new ArrayList<>();
        return execute1(page, size, list)
                .map(b -> {
                    if (b) {
                        P6eReactivePermissionCache.cache(list);
                    }
                    return b;
                });
    }

    private static Mono<Boolean> execute1(int page, int size, List<PermissionModel> list) {
        return execute2(page, size, list)
                .map(c -> c == size)
                .flatMap(b -> b ? execute1(page + 1, size, list) : Mono.just(true))
                .onErrorResume(t -> Mono.just(false));
    }

    private static Mono<Integer> execute2(int page, int size, List<PermissionModel> list) {
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
