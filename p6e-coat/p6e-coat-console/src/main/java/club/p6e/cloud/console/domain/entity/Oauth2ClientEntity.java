package club.p6e.cloud.console.domain.entity;

import club.p6e.cloud.console.error.GlobalExceptionContext;
import club.p6e.cloud.console.infrastructure.model.Oauth2ClientModel;
import club.p6e.cloud.console.infrastructure.repository.Oauth2ClientRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.GeneratorUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import org.springframework.util.DigestUtils;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2ClientEntity implements Serializable {

    private final Oauth2ClientModel model;

    public static Oauth2ClientEntity findById(Integer id) {
        final Oauth2ClientRepository repository = SpringUtil.getBean(Oauth2ClientRepository.class);
        final Optional<Oauth2ClientModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    Oauth2ClientEntity.class,
                    "",
                    ""
            );
        } else {
            return new Oauth2ClientEntity(optional.get());
        }
    }

    private Oauth2ClientEntity(Oauth2ClientModel model) {
        this.model = model;
    }

    public static Oauth2ClientEntity create(Oauth2ClientModel model) {
        final Oauth2ClientRepository repository = SpringUtil.getBean(Oauth2ClientRepository.class);
        final String id = "CLIENT_ID_" + GeneratorUtil.uuid() + GeneratorUtil.random(6);
        final String md5Id = DigestUtils.md5DigestAsHex(id.getBytes(StandardCharsets.UTF_8));
        final String secret = "CLIENT_SECRET_" + GeneratorUtil.uuid() + GeneratorUtil.random(6);
        final String md5Secret = DigestUtils.md5DigestAsHex(secret.getBytes(StandardCharsets.UTF_8));
        model.setId(null);
        model.setClientId(md5Id);
        model.setClientSecret(md5Secret);
        return new Oauth2ClientEntity(repository.saveAndFlush(model));
    }

    public Oauth2ClientModel getModel() {
        return model;
    }

    public Oauth2ClientEntity update(Oauth2ClientModel model) {
        final Oauth2ClientRepository repository = SpringUtil.getBean(Oauth2ClientRepository.class);
        final Optional<Oauth2ClientModel> optional = repository.findById(this.model.getId());
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    Oauth2ClientEntity.class,
                    "",
                    ""
            );
        } else {
            model.setId(null);
            model.setClientId(null);
            model.setClientSecret(null);
            repository.saveAndFlush(CopyUtil.run(model, optional.get()));
            return new Oauth2ClientEntity(optional.get());
        }
    }

    public Oauth2ClientEntity delete() {
        final Oauth2ClientRepository repository = SpringUtil.getBean(Oauth2ClientRepository.class);
        final Optional<Oauth2ClientModel> optional = repository.findById(this.model.getId());
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    Oauth2ClientEntity.class,
                    "",
                    ""
            );
        } else {
            repository.delete(optional.get());
            return new Oauth2ClientEntity(optional.get());
        }
    }
}
