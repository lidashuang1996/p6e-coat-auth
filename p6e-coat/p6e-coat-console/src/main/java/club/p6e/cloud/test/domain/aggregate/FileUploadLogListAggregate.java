package club.p6e.cloud.test.domain.aggregate;

import club.p6e.cloud.test.domain.ConfigurationDomain;
import club.p6e.cloud.test.infrastructure.model.FileUploadModel;
import club.p6e.cloud.test.infrastructure.repository.FileUploadRepository;
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
public class FileUploadLogListAggregate extends ConfigurationDomain {

    private final int page;
    private final int size;
    private final long total;
    private final List<FileUploadModel> list;

    public static FileUploadLogListAggregate search(Integer page, Integer size, String query) {
        page = initPage(page);
        size = initSize(size);
        final FileUploadRepository repository = SpringUtil.getBean(FileUploadRepository.class);
        final Page<FileUploadModel> pfm = repository.findAll((Specification<FileUploadModel>) (rt, qy, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            if (query != null) {
                final String lq = "%" + query + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(rt.get(FileUploadModel.NAME)), lq)
                ));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        }, PageRequest.of(page - 1, size,  Sort.by(Sort.Order.desc(FileUploadModel.ID))));
        return new FileUploadLogListAggregate(page, size, pfm.getTotalElements(), new ArrayList<>(pfm.getContent()));
    }

    private FileUploadLogListAggregate(int page, int size, long total, List<FileUploadModel> list) {
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

    public List<FileUploadModel> getList() {
        return list;
    }

}
