package club.p6e.cloud.console.domain.aggregate;

import club.p6e.cloud.console.domain.ConfigurationDomain;
import club.p6e.cloud.console.error.GlobalExceptionContext;
import club.p6e.cloud.console.infrastructure.model.PermissionUrlGroupModel;
import club.p6e.cloud.console.infrastructure.model.PermissionUrlGroupRelationUrlModel;
import club.p6e.cloud.console.infrastructure.model.PermissionUrlModel;
import club.p6e.cloud.console.infrastructure.repository.PermissionUrlGroupRelationUrlRepository;
import club.p6e.cloud.console.infrastructure.repository.PermissionUrlGroupRepository;
import club.p6e.cloud.console.infrastructure.repository.PermissionUrlRepository;
import com.darvi.hksi.badminton.lib.utils.SpringUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author lidashuang
 * @version 1.0
 */
public class PermissionUrlGroupDetailsAggregate extends ConfigurationDomain {
    private final PermissionUrlGroupModel model;
    private final List<PermissionUrlModel> list;
    private final int page;
    private final int size;
    private final long total;

    public static PermissionUrlGroupDetailsAggregate get(Integer id, Integer page, Integer size) {
        page = initPage(page);
        size = initSize(size);
        final PermissionUrlRepository uRepository = SpringUtil.getBean(PermissionUrlRepository.class);
        final PermissionUrlGroupRepository ugRepository = SpringUtil.getBean(PermissionUrlGroupRepository.class);
        final PermissionUrlGroupRelationUrlRepository urgRepository = SpringUtil.getBean(PermissionUrlGroupRelationUrlRepository.class);
        final Optional<PermissionUrlGroupModel> optional = ugRepository.findById(id);
        if (optional.isEmpty()) {
            throw GlobalExceptionContext.executeParameterException(
                    PermissionUrlGroupDetailsAggregate.class,
                    "",
                    ""
            );
        }
        final List<PermissionUrlModel> list = new ArrayList<>();
        final Page<PermissionUrlGroupRelationUrlModel> urgPage = urgRepository.findAll(
                (Specification<PermissionUrlGroupRelationUrlModel>)
                        (root, query, cb) -> cb.equal(root.get(PermissionUrlGroupRelationUrlModel.GID), id),
                PageRequest.of(page - 1, size, Sort.by(Sort.Order.asc(PermissionUrlGroupRelationUrlModel.UID)))
        );
        for (final PermissionUrlGroupRelationUrlModel item : urgPage.getContent()) {
            final Optional<PermissionUrlModel> oum = uRepository.findById(item.getUid());
            oum.ifPresent(permissionUrlModel -> list.add(permissionUrlModel
                    .setRelationConfig(item.getConfig())
                    .setRelationAttribute(item.getAttribute())
            ));
        }
        return new PermissionUrlGroupDetailsAggregate(optional.get(), list, page, size, urgPage.getTotalElements());
    }

    private PermissionUrlGroupDetailsAggregate(PermissionUrlGroupModel model,
                                               List<PermissionUrlModel> list,
                                               int page, int size, long total) {
        this.page = page;
        this.size = size;
        this.total = total;
        this.list = list;
        this.model = model;
    }

    public PermissionUrlGroupModel getModel() {
        return model;
    }

    public List<PermissionUrlModel> getList() {
        return list;
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
}
