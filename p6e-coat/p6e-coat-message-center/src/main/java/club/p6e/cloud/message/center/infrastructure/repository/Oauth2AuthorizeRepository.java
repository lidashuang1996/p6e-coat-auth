package club.p6e.cloud.message.center.infrastructure.repository;

import club.p6e.cloud.console.infrastructure.model.Oauth2AuthorizeKeyModel;
import club.p6e.cloud.console.infrastructure.model.Oauth2AuthorizeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2AuthorizeRepository extends
        JpaRepository<Oauth2AuthorizeModel, Oauth2AuthorizeKeyModel>,
        JpaSpecificationExecutor<Oauth2AuthorizeModel> {
}
