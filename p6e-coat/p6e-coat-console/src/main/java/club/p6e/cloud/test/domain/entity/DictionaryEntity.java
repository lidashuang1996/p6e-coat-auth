package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.DictionaryModel;
import club.p6e.cloud.test.infrastructure.model.UserModel;
import club.p6e.cloud.test.infrastructure.repository.DictionaryRepository;
import club.p6e.cloud.test.infrastructure.repository.UserRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;

import java.io.Serializable;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class DictionaryEntity implements Serializable {

    private final DictionaryModel model;

    public static DictionaryEntity findById(Integer id) {
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        final Optional<DictionaryModel> optional = repository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    DictionaryEntity.class,
                    "",
                    ""
            );
        } else {
            return new DictionaryEntity(optional.get());
        }
    }

    public static DictionaryEntity create(DictionaryModel model) {
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        return new DictionaryEntity(repository.saveAndFlush(model));
    }

    private DictionaryEntity(DictionaryModel model) {
        this.model = model;
    }

    public DictionaryEntity update(DictionaryModel um) {
        um.setId(null);
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        return new DictionaryEntity(repository.saveAndFlush(CopyUtil.run(um, model)));
    }

    public DictionaryEntity delete() {
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        final Optional<DictionaryModel> optional = repository.findById(model.getId());
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.exceptionAccountException(
                    DictionaryEntity.class,
                    "",
                    ""
            );
        } else {
            repository.delete(optional.get());
            return new DictionaryEntity(optional.get());
        }
    }

    public DictionaryModel getModel() {
        return model;
    }

}
