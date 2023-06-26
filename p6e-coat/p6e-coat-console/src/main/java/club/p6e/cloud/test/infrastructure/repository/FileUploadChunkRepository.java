package club.p6e.cloud.test.infrastructure.repository;

import club.p6e.cloud.test.infrastructure.model.FileUploadChunkModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface FileUploadChunkRepository extends
        JpaRepository<FileUploadChunkModel, Integer>,
        JpaSpecificationExecutor<FileUploadChunkModel> {
}
