package club.p6e.cloud.message.center.infrastructure.repository;

import club.p6e.cloud.message.center.infrastructure.model.LauncherModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface DictionaryRepository extends
        JpaRepository<LauncherModel, Integer>,
        JpaSpecificationExecutor<LauncherModel> {
}
