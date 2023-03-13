package club.p6e.coat.gateway.auth.repository;

import com.darvi.smart.construction.web.infrastructure.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author  hjf
 */
public interface UserRepository extends JpaRepository<UserModel,Integer> {
    /**
     * getProjectUserList
     *
     * @param pid
     * @return 结果对象
     */
    @Query(value = "SELECT * FROM sc_user a,sc_project_relation_user b WHERE a.id=b.uid and b.pid = ?1 ORDER BY a.name",nativeQuery = true)
    List<UserModel> getProjectUserList(Integer pid);

    /**
     * findAllByNameContains
     *
     * @param name
     * @return 结果对象
     */
    List<UserModel> findAllByNameContains(String name);
}
