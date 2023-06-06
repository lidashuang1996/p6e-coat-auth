package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.PermissionUrlModel;
import club.p6e.cloud.test.infrastructure.repository.PermissionUrlRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;

import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PermissionUrlEntity {

    private final PermissionUrlModel model;

    public static PermissionUrlEntity findById(Integer id) {
        final PermissionUrlRepository repository = SpringUtil.getBean(PermissionUrlRepository.class);
        final Optional<PermissionUrlModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeUserNotExistException(
                    PermissionUrlEntity.class,
                    "",
                    ""
            );
        } else {
            return new PermissionUrlEntity(optional.get());
        }
    }

    public static PermissionUrlEntity create(PermissionUrlModel model) {
        final PermissionUrlRepository repository = SpringUtil.getBean(PermissionUrlRepository.class);
        return new PermissionUrlEntity(repository.saveAndFlush(model));
    }

    private PermissionUrlEntity(PermissionUrlModel model) {
        this.model = model;
    }

    public PermissionUrlEntity update(PermissionUrlModel um) {
        um.setId(null);
        final PermissionUrlRepository repository = SpringUtil.getBean(PermissionUrlRepository.class);
        return new PermissionUrlEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public PermissionUrlEntity delete() {
        final PermissionUrlRepository repository = SpringUtil.getBean(PermissionUrlRepository.class);
        return new PermissionUrlEntity(repository.saveAndFlush(model.setIsDelete(1)));
    }

    public PermissionUrlModel getModel() {
        return model;
    }

}
