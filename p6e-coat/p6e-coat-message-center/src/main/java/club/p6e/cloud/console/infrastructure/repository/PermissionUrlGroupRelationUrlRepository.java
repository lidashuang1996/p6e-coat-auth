package club.p6e.cloud.console.infrastructure.repository;

import club.p6e.cloud.console.infrastructure.model.PermissionUrlGroupRelationUrlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionUrlGroupRelationUrlRepository
        extends JpaRepository<PermissionUrlGroupRelationUrlModel, PermissionUrlGroupRelationUrlModel.Key>,
        JpaSpecificationExecutor<PermissionUrlGroupRelationUrlModel> {
}
