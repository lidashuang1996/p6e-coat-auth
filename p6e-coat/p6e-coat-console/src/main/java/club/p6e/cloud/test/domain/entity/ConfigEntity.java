package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.ConfigModel;
import club.p6e.cloud.test.infrastructure.model.UserModel;
import club.p6e.cloud.test.infrastructure.repository.ConfigRepository;
import club.p6e.cloud.test.infrastructure.repository.UserRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ConfigEntity implements Serializable {

    private final ConfigModel model; 

    public static ConfigEntity findById(Integer id) {
        final ConfigRepository repository = SpringUtil.getBean(ConfigRepository.class);
        final Optional<ConfigModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    ConfigEntity.class,
                    "",
                    ""
            );
        } else {
            return new ConfigEntity(optional.get());
        }
    }

    public static ConfigEntity create(ConfigModel model) {
        final ConfigRepository repository = SpringUtil.getBean(ConfigRepository.class);
        return new ConfigEntity(repository.saveAndFlush(model));
    }

    private ConfigEntity(ConfigModel model) {
        this.model = model;
    }


    public ConfigEntity update(ConfigModel cm) {
        cm.setId(null);
        final ConfigRepository repository = SpringUtil.getBean(ConfigRepository.class);
        return new ConfigEntity(repository.saveAndFlush(CopyUtil.run(cm, model)));
    }

    public ConfigEntity delete() {
        final ConfigRepository repository = SpringUtil.getBean(ConfigRepository.class);
        return new ConfigEntity(repository.saveAndFlush(model.setIsDelete(1)));
    }

    public ConfigModel getModel() {
        return model;
    }

}
