package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.PermissionUrlGroupModel;
import club.p6e.cloud.test.infrastructure.repository.PermissionUrlGroupRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PermissionUrlGroupEntity {

    private final PermissionUrlGroupModel model;

    public static PermissionUrlGroupEntity findById(Integer id) {
        final PermissionUrlGroupRepository repository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        final Optional<PermissionUrlGroupModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeUserNotExistException(
                    PermissionUrlEntity.class,
                    "",
                    ""
            );
        } else {
            return new PermissionUrlGroupEntity(optional.get());
        }
    }

    public static PermissionUrlGroupEntity create(PermissionUrlGroupModel model) {
        final PermissionUrlGroupRepository repository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        return new PermissionUrlGroupEntity(repository.saveAndFlush(model));
    }

    private PermissionUrlGroupEntity(PermissionUrlGroupModel model) {
        this.model = model;
    }

    public PermissionUrlGroupEntity update(PermissionUrlGroupModel um) {
        um.setId(null);
        final PermissionUrlGroupRepository repository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        return new PermissionUrlGroupEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public PermissionUrlGroupEntity delete() {
        final PermissionUrlGroupRepository repository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        return new PermissionUrlGroupEntity(repository.saveAndFlush(model.setIsDelete(1)));
    }

    public PermissionUrlGroupModel getModel() {
        return model;
    }

}
