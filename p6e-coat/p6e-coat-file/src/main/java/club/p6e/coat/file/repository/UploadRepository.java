package club.p6e.coat.file.repository;

import club.p6e.coat.file.model.UploadModel;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 文件上传存储库
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = UploadRepository.class,
        ignored = UploadRepository.class
)
public class UploadRepository extends BaseRepository {

    /**
     * R2dbcEntityTemplate 对象
     */
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    /**
     * 构造方法初始化
     *
     * @param r2dbcEntityTemplate R2dbcEntityTemplate 对象
     */
    public UploadRepository(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    /**
     * 创建数据
     *
     * @param model 模型对象
     * @return Mono<UploadModel> 模型对象
     */
    public Mono<UploadModel> create(UploadModel model) {
        if (model == null) {
            throw new NullPointerException(UploadRepository.class
                    + " create(). " + UploadModel.class + " => model is null ! ");
        }
        if (model.getSize() == null) {
            model.setSize(0);
        }
        if (model.getOperator() == null) {
            model.setOperator("sys");
        }
        model.setId(null);
        model.setLock(0);
        model.setVersion(0);
        model.setCreateDate(LocalDateTime.now());
        model.setUpdateDate(LocalDateTime.now());
        return r2dbcEntityTemplate.insert(model);
    }

    /**
     * 修改数据--锁增加 1
     *
     * @param model 模型对象
     * @return Mono<Long> 结果对象
     */
    public Mono<Long> acquireLock(UploadModel model) {
        return r2dbcEntityTemplate.update(UploadModel.class)
                .matching(Query.query(
                        Criteria.where(UploadModel.ID).is(model.getId())
                                .and(UploadModel.VERSION).is(model.getVersion())))
                .apply(Update.update(UploadModel.VERSION, model.getVersion() + 1)
                        .set(UploadModel.LOCK, model.getLock() + 1));
    }

    /**
     * 修改数据--锁增加 1
     *
     * @param model 模型对象
     * @param retry 重试次数
     * @return Mono<Long> 结果对象
     */
    public Mono<Long> acquireLock(UploadModel model, int retry) {
        if (retry == 0) {
            return acquireLock(model)
                    .flatMap(c -> c > 0 ? Mono.just(c) : acquireLock(model, (retry + 1)));
        } else if (retry <= MAX_RETRY_COUNT) {
            final long interval = RETRY_INTERVAL_DATE * ThreadLocalRandom.current().nextInt(100) / 100;
            return Mono.delay(Duration.of(interval, ChronoUnit.MICROS))
                    .flatMap(r -> acquireLock(model))
                    .flatMap(c -> c > 0 ? Mono.just(c) : acquireLock(model, (retry + 1)));
        } else {
            return Mono.just(0L);
        }
    }

    /**
     * 修改数据--锁减少 1
     *
     * @param model 模型对象
     * @return Mono<Long> 结果对象
     */
    public Mono<Long> releaseLock(UploadModel model) {
        return r2dbcEntityTemplate.update(UploadModel.class)
                .matching(Query.query(
                        Criteria.where(UploadModel.ID).is(model.getId())
                                .and(UploadModel.VERSION).is(model.getVersion())))
                .apply(Update.update(UploadModel.VERSION, model.getVersion() + 1)
                        .set(UploadModel.LOCK, model.getLock() - 1));
    }

    /**
     * 修改数据--锁减少 1
     *
     * @param model 模型对象
     * @param retry 重试次数
     * @return Mono<Long> 结果对象
     */
    public Mono<Long> releaseLock(UploadModel model, int retry) {
        if (retry == 0) {
            return releaseLock(model)
                    .flatMap(c -> c > 0 ? Mono.just(c) : releaseLock(model, (retry + 1)));
        } else if (retry <= MAX_RETRY_COUNT) {
            final long interval = RETRY_INTERVAL_DATE * ThreadLocalRandom.current().nextInt(100) / 100;
            return Mono.delay(Duration.of(interval, ChronoUnit.MICROS))
                    .flatMap(r -> releaseLock(model))
                    .flatMap(c -> c > 0 ? Mono.just(c) : releaseLock(model, (retry + 1)));
        } else {
            return Mono.just(0L);
        }
    }

    /**
     * 关闭锁
     *
     * @param id 模型 ID
     * @return Mono<UploadModel> 模型对象
     */
    public Mono<UploadModel> closeLock(Integer id) {
        return r2dbcEntityTemplate.selectOne(Query.query(
                        Criteria.where(UploadModel.ID).is(id)), UploadModel.class)
                .flatMap(m -> {
                    if (m.getLock() == 0) {
                        return r2dbcEntityTemplate.update(UploadModel.class)
                                .matching(Query.query(
                                        Criteria.where(UploadModel.ID).is(m.getId())
                                                .and(UploadModel.VERSION).is(m.getVersion())))
                                .apply(Update.update(UploadModel.VERSION, m.getVersion() + 1).set(UploadModel.LOCK, -1))
                                .flatMap(c -> {
                                    if (c > 0) {
                                        m.setLock(-1);
                                        m.setVersion(m.getVersion());
                                        return Mono.just(m);
                                    } else {
                                        return Mono.empty();
                                    }
                                });
                    } else {
                        return Mono.empty();
                    }
                });
    }

    /**
     * 关闭锁
     *
     * @param id    模型 ID
     * @param retry 重试次数
     * @return Mono<UploadModel> 模型对象
     */
    public Mono<UploadModel> closeLock(Integer id, int retry) {
        if (retry == 0) {
            return closeLock(id)
                    .switchIfEmpty(closeLock(id, (retry + 1)));
        } else if (retry <= MAX_RETRY_COUNT) {
            final long interval = RETRY_INTERVAL_DATE * ThreadLocalRandom.current().nextInt(100) / 100;
            return Mono.delay(Duration.of(interval, ChronoUnit.MICROS))
                    .flatMap(t -> closeLock(id))
                    .switchIfEmpty(closeLock(id, (retry + 1)));
        } else {
            return Mono.empty();
        }
    }

    /**
     * 根据 ID 查询数据
     *
     * @param id 模型 ID
     * @return Mono<UploadModel> 模型对象
     */
    public Mono<UploadModel> findById(Integer id) {
        return r2dbcEntityTemplate.selectOne(Query.query(
                Criteria.where(UploadModel.ID).is(id)), UploadModel.class);
    }

    /**
     * 修改数据
     *
     * @param model 模型对象
     * @return Mono<UploadModel> 模型对象
     */
    public Mono<Long> update(UploadModel model) {
        return r2dbcEntityTemplate.update(UploadModel.class)
                .matching(Query.query(
                        Criteria.where(UploadModel.ID).is(model.getId())
                                .and(UploadModel.VERSION).is(model.getVersion())))
                .apply(Update.update(UploadModel.SIZE, model.getSize()));
    }

}
