package club.p6e.cloud.test.domain.aggregate;

import club.p6e.cloud.test.infrastructure.converter.SearchableConverter;
import club.p6e.cloud.test.infrastructure.converter.SortableConverter;
import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.infrastructure.model.ConfigModel;
import club.p6e.cloud.test.infrastructure.repository.ConfigRepository;
import com.darvi.hksi.badminton.lib.SearchableContext;
import com.darvi.hksi.badminton.lib.SortableContext;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ConfigListAggregate extends ConfigurationDomain {

    private final int page;
    private final int size;
    private final long total;
    private final List<ConfigModel> list;

    public static ConfigListAggregate search(
            Integer page, Integer size, String query,
            SortableContext<SortableContext.Option> sort,
            SearchableContext<SearchableContext.Option> search) {
        page = initPage(page);
        size = initSize(size);
        final ConfigRepository repository = SpringUtil.getBean(ConfigRepository.class);
        final Page<ConfigModel> pcm = repository.findAll((Specification<ConfigModel>) (rt, qy, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (query != null) {
                final String lq = "%" + query + "%";
                predicates.add(cb.or(
                        cb.like(cb.function(
                                "text",
                                String.class,
                                rt.get(ConfigModel.ID)
                        ), lq),
                        cb.like(cb.lower(rt.get(ConfigModel.KEY)), lq),
                        cb.like(cb.lower(rt.get(ConfigModel.VALUE)), lq)
                ));
            }
            final Predicate sp = SearchableConverter.to(search, rt, cb);
            if (sp != null) {
                predicates.add(sp);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size, SortableConverter.to(sort, Sort.by(Sort.Order.asc(ConfigModel.ID)))));
        return new ConfigListAggregate(page, size, pcm.getTotalElements(), new ArrayList<>(pcm.getContent()));
    }

    private ConfigListAggregate(int page, int size, long total, List<ConfigModel> list) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public List<ConfigModel> getList() {
        return list;
    }

}
