package club.p6e.coat.file.repository;

import club.p6e.coat.file.model.UploadModel;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.data.relational.core.query.Update;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * 文件上传存储库
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
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
     * @return Mono<UploadRepository> 模型对象
     */
    public Mono<UploadModel> create(UploadModel model) {
        if (model == null) {
            throw new NullPointerException(UploadRepository.class
                    + " create(). " + UploadModel.class + " => model is null ! ");
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
     * 修改数据--锁
     *
     * @param model 模型对象
     * @param bool  true 增加 false 减少
     * @return Mono<ResourceUploadModel> 模型对象
     */
    public Mono<Long> acquireLock(UploadModel model, boolean bool) {
        return r2dbcEntityTemplate.update(UploadModel.class)
                .matching(Query.query(
                        Criteria.where(UploadModel.ID).is(model.getId())
                                .and(UploadModel.LOCK).is(model.getLock())
                                .and(UploadModel.VERSION).is(model.getVersion())))
                .apply(Update.update(UploadModel.VERSION, model.getVersion() + 1).set(
                        UploadModel.LOCK, (bool ? (model.getLock() + 1) : (model.getLock() - 1))));
    }

    /**
     * 修改数据--锁
     *
     * @param model 模型对象
     * @return Mono<Integer> 模型对象
     */
    public Mono<Long> releaseLock(UploadModel model) {
        return r2dbcEntityTemplate.update(UploadModel.class)
                .matching(Query.query(
                        Criteria.where(UploadModel.ID).is(model.getId())
                                .and(UploadModel.LOCK).is(model.getLock())
                                .and(UploadModel.VERSION).is(model.getVersion())))
                .apply(Update.update(UploadModel.VERSION, model.getVersion() + 1).set(UploadModel.LOCK, -1));
    }

    /**
     * 根据 ID 查询数据
     *
     * @param id 模型 ID
     * @return Mono<ResourceUploadModel> 模型对象
     */
    public Mono<UploadModel> findById(Integer id) {
        return r2dbcEntityTemplate.selectOne(Query.query(
                Criteria.where(UploadModel.ID).is(id)), UploadModel.class);
    }

    public Mono<UploadModel> closeLock(Integer id) {
        return r2dbcEntityTemplate.selectOne(Query.query(
                Criteria.where(UploadModel.ID).is(id)), UploadModel.class);
    }

}
