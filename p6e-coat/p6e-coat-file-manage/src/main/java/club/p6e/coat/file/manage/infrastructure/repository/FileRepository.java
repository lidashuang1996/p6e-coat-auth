package club.p6e.coat.file.manage.infrastructure.repository;

import club.p6e.coat.file.manage.infrastructure.model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author lidashuang
 * @version 1.0
 */
public interface FileRepository extends JpaRepository<FileModel, Integer>, JpaSpecificationExecutor<FileModel> {
}