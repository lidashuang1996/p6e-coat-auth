package club.p6e.cloud.console.domain.entity;

import club.p6e.cloud.console.error.GlobalExceptionContext;
import club.p6e.cloud.console.infrastructure.model.UserModel;
import club.p6e.cloud.console.infrastructure.repository.UserRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class UserEntity implements Serializable {

    private final UserModel model;

    public static UserEntity findById(Integer id) {
        final UserRepository repository = SpringUtil.getBean(UserRepository.class);
        final Optional<UserModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeUserNotExistException(
                    UserEntity.class,
                    "",
                    ""
            );
        } else {
            return new UserEntity(optional.get());
        }
    }

    public static UserEntity create(UserModel model) {
        final UserRepository repository = SpringUtil.getBean(UserRepository.class);
        return new UserEntity(repository.saveAndFlush(model));
    }

    private UserEntity(UserModel model) {
        this.model = model;
    }

    public UserEntity update(UserModel um) {
        um.setId(null);
        final UserRepository repository = SpringUtil.getBean(UserRepository.class);
        return new UserEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public UserEntity delete() {
        final UserRepository repository = SpringUtil.getBean(UserRepository.class);
        return new UserEntity(repository.saveAndFlush(model.setIsDelete(1)));
    }

    public UserModel getModel() {
        return model;
    }

}
