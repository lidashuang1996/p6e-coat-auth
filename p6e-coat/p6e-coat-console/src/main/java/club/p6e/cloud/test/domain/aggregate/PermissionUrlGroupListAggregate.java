package club.p6e.cloud.test.domain.aggregate;

import club.p6e.cloud.test.infrastructure.converter.SearchableConverter;
import club.p6e.cloud.test.infrastructure.converter.SortableConverter;
import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.infrastructure.model.PermissionUrlGroupModel;
import club.p6e.cloud.test.infrastructure.repository.PermissionUrlGroupRepository;
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
public class PermissionUrlGroupListAggregate extends ConfigurationDomain {

    private final int page;
    private final int size;
    private final long total;
    private final List<PermissionUrlGroupModel> list;

    public static PermissionUrlGroupListAggregate search(
            Integer page, Integer size, String query,
            SortableContext<SortableContext.Option> sort,
            SearchableContext<SearchableContext.Option> search) {
        page = initPage(page);
        size = initSize(size);
        final PermissionUrlGroupRepository repository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        final Page<PermissionUrlGroupModel> pcm = repository.findAll((Specification<PermissionUrlGroupModel>) (rt, qy, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (query != null) {
                final String lq = "%" + query + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(rt.get(PermissionUrlGroupModel.NAME)), lq),
                        cb.like(cb.lower(rt.get(PermissionUrlGroupModel.DESCRIBE)), lq),
                        cb.like(cb.lower(rt.get(PermissionUrlGroupModel.MARK)), lq)
                ));
            }
            predicates.add(SearchableConverter.to(search, rt, cb));
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size, SortableConverter.to(sort, Sort.by(Sort.Order.asc(PermissionUrlGroupModel.ID)))));
        return new PermissionUrlGroupListAggregate(page, size, pcm.getTotalElements(), new ArrayList<>(pcm.getContent()));
    }

    private PermissionUrlGroupListAggregate(int page, int size, long total, List<PermissionUrlGroupModel> list) {
        this.list = list;
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public int getPage() {
        return page;
    }

    public long getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public List<PermissionUrlGroupModel> getList() {
        return list;
    }

}
