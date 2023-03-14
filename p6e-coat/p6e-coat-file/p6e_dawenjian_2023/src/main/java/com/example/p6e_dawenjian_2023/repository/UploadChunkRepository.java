package com.example.p6e_dawenjian_2023.repository;

import com.example.p6e_dawenjian_2023.model.UploadChunkModel;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 文件块上传存储库
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class UploadChunkRepository extends BaseRepository {

    /**
     * R2dbcEntityTemplate 对象
     */
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    /**
     * 构造方法初始化
     *
     * @param r2dbcEntityTemplate R2dbcEntityTemplate 对象
     */
    public UploadChunkRepository(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    /**
     * 创建数据
     *
     * @param model 模型对象
     * @return Mono<UploadChunkModel> 模型对象
     */
    public Mono<UploadChunkModel> create(UploadChunkModel model) {
        return r2dbcEntityTemplate.insert(model);
    }

    /**
     * 根据 FID 查询数据
     *
     * @param fid FID
     * @return Mono<UploadChunkModel> 模型对象
     */
    public Mono<List<UploadChunkModel>> findByFid(Integer fid) {
        return r2dbcEntityTemplate.select(Query.query(Criteria.where(
                UploadChunkModel.FID).is(fid)), UploadChunkModel.class).collectList();
    }

}
