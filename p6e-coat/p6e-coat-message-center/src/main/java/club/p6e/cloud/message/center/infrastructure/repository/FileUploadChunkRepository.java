package club.p6e.cloud.message.center.infrastructure.repository;

import club.p6e.cloud.message.center.infrastructure.model.MapperModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface FileUploadChunkRepository extends
        JpaRepository<MapperModel, Integer>,
        JpaSpecificationExecutor<MapperModel> {
}
