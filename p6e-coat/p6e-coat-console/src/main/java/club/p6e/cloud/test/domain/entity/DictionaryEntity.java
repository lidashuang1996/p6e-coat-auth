package club.p6e.cloud.test.domain.entity;

import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.model.DictionaryModel;
import club.p6e.cloud.test.infrastructure.repository.DictionaryRepository;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class DictionaryEntity extends ConfigurationDomain implements Serializable {

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

    public static Map<String, Map<String, String>> type(List<String> types, String language) {
        final String nLanguage = language == null ? DEFAULT_LANGUAGE : language;
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        final List<DictionaryModel> dList = repository.findAll(
                (Specification<DictionaryModel>) (root, query, cb) -> cb.and(
                        cb.in(root.get(DictionaryModel.TYPE)).value(types),
                        cb.equal(root.get(DictionaryModel.LANGUAGE), nLanguage)
                ));
        final Map<String, Map<String, String>> result = new HashMap<>();
        for (final DictionaryModel item : dList) {
            result.computeIfAbsent(item.getType(), k -> new HashMap<>());
            result.get(item.getType()).put(item.getKey(), item.getValue());
        }
        return result;
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
