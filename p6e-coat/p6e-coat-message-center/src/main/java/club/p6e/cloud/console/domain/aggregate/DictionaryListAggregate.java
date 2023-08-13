package club.p6e.cloud.console.domain.aggregate;

import club.p6e.cloud.console.infrastructure.converter.SearchableConverter;
import club.p6e.cloud.console.infrastructure.converter.SortableConverter;
import club.p6e.cloud.console.domain.ConfigurationDomain;
import club.p6e.cloud.console.infrastructure.model.DictionaryModel;
import club.p6e.cloud.console.infrastructure.repository.DictionaryRepository;
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
public class DictionaryListAggregate extends ConfigurationDomain {

    private final int page;
    private final int size;
    private final long total;
    private final List<DictionaryModel> list;

    public static DictionaryListAggregate search(
            Integer page, Integer size, String query,
            SortableContext<SortableContext.Option> sort,
            SearchableContext<SearchableContext.Option> search) {
        page = initPage(page);
        size = initSize(size);
        final DictionaryRepository repository = SpringUtil.getBean(DictionaryRepository.class);
        final Page<DictionaryModel> pcm = repository.findAll((Specification<DictionaryModel>) (rt, qy, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (query != null) {
                final String lq = "%" + query + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(rt.get(DictionaryModel.KEY)), lq),
                        cb.like(cb.lower(rt.get(DictionaryModel.VALUE)), lq),
                        cb.like(cb.lower(rt.get(DictionaryModel.TYPE)), lq)
                ));
            }
            final Predicate sp = SearchableConverter.to(search, rt, cb);
            if (sp != null) {
                predicates.add(sp);
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size, SortableConverter.to(sort, Sort.by(Sort.Order.asc(DictionaryModel.ID)))));
        return new DictionaryListAggregate(page, size, pcm.getTotalElements(), new ArrayList<>(pcm.getContent()));
    }

    private DictionaryListAggregate(int page, int size, long total, List<DictionaryModel> list) {
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

    public List<DictionaryModel> getList() {
        return list;
    }

}
