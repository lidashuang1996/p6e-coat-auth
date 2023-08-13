package club.p6e.cloud.console.application.service.impl;

import club.p6e.cloud.console.application.service.DictionaryService;
import club.p6e.cloud.console.domain.aggregate.DictionaryListAggregate;
import club.p6e.cloud.console.domain.entity.DictionaryEntity;
import club.p6e.cloud.console.infrastructure.context.DictionaryContext;
import club.p6e.cloud.console.infrastructure.model.DictionaryModel;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class DictionaryServiceImpl implements DictionaryService {
    @Override
    public DictionaryContext.ListDto list(DictionaryContext.Request request) {
        final DictionaryListAggregate aggregate = DictionaryListAggregate.search(
                request.getPage(), request.getSize(),
                request.getQuery(), request.getSort(), request.getSearch()
        );
        final DictionaryContext.ListDto result = new DictionaryContext.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setList(CopyUtil.runList(aggregate.getList(), DictionaryContext.Item.class));
        return result;
    }

    @Override
    public DictionaryContext.Dto create(DictionaryContext.Request request) {
        return CopyUtil.run(DictionaryEntity.create(
                CopyUtil.run(request, DictionaryModel.class)
        ).getModel(), DictionaryContext.Dto.class);
    }

    @Override
    public DictionaryContext.Dto get(DictionaryContext.Request request) {
        return CopyUtil.run(DictionaryEntity.findById(request.getId()).getModel(), DictionaryContext.Dto.class);
    }

    @Override
    public DictionaryContext.Dto delete(DictionaryContext.Request request) {
        return CopyUtil.run(DictionaryEntity.findById(request.getId()).delete().getModel(), DictionaryContext.Dto.class);
    }

    @Override
    public DictionaryContext.Dto update(DictionaryContext.Request request) {
        return CopyUtil.run(DictionaryEntity.findById(request.getId()).update(
                CopyUtil.run(request, DictionaryModel.class)
        ).getModel(), DictionaryContext.Dto.class);
    }

    @Override
    public DictionaryContext.Type.Dto type(DictionaryContext.Type.Request request) {
        return new DictionaryContext.Type.Dto().setData(
                DictionaryEntity.type(request.getTypes(), request.getLanguage()));
    }
}
