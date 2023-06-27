package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.UserAuthModel;
import club.p6e.cloud.test.infrastructure.model.UserModel;
import club.p6e.cloud.test.infrastructure.repository.UserAuthRepository;
import club.p6e.cloud.test.infrastructure.repository.UserRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class UserAuthEntity implements Serializable {

    public static String executePasswordEncryptor(String pwd) {
        return pwd + "123456";
    }

    private final UserAuthModel model;

    public static UserAuthEntity findById(Integer id) {
        final UserAuthRepository repository = SpringUtil.getBean(UserAuthRepository.class);
        final Optional<UserAuthModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeUserNotExistException(
                    UserAuthEntity.class,
                    "",
                    ""
            );
        } else {
            return new UserAuthEntity(optional.get());
        }
    }

    private UserAuthEntity(UserAuthModel model) {
        this.model = model;
    }

    public UserAuthEntity update(UserAuthModel um) {
        um.setId(null);
        um.setPassword(um.getPassword());
        final UserAuthRepository repository = SpringUtil.getBean(UserAuthRepository.class);
        return new UserAuthEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public boolean verifyPassword(String password) {
        return this.model.getPassword().equals(password);
    }

    public UserAuthModel getModel() {
        return model;
    }

}
