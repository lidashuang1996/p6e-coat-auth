package club.p6e.cloud.console.domain.entity;

import club.p6e.cloud.console.PasswordEncryptor;
import club.p6e.cloud.console.error.GlobalExceptionContext;
import club.p6e.cloud.console.infrastructure.model.UserAuthModel;
import club.p6e.cloud.console.infrastructure.repository.UserAuthRepository;
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
        final PasswordEncryptor encryptor = SpringUtil.getBean(PasswordEncryptor.class);
        final UserAuthRepository repository = SpringUtil.getBean(UserAuthRepository.class);
        um.setId(null);
        um.setPassword(encryptor.execute(um.getPassword()));
        return new UserAuthEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public boolean verifyPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        } else {
            final PasswordEncryptor encryptor = SpringUtil.getBean(PasswordEncryptor.class);
            return this.model.getPassword().equals(encryptor.execute(password));
        }
    }

    public UserAuthModel getModel() {
        return model;
    }

}
