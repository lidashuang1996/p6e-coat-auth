package club.p6e.cloud.console.infrastructure.repository;

import club.p6e.cloud.console.infrastructure.model.Oauth2ClientModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2ClientRepository extends
        JpaRepository<Oauth2ClientModel, Integer>,
        JpaSpecificationExecutor<Oauth2ClientModel> {
}
