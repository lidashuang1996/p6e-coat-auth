package club.p6e.cloud.message.center.infrastructure.repository;

import club.p6e.cloud.console.infrastructure.model.UserAuthModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface UserAuthRepository extends
        JpaRepository<UserAuthModel, Integer>,
        JpaSpecificationExecutor<UserAuthModel> {
}
