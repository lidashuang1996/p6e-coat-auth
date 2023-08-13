package club.p6e.cloud.console.application.service.impl;

import club.p6e.cloud.console.application.service.PermissionServer;
import club.p6e.cloud.console.domain.aggregate.PermissionUrlGroupDetailsAggregate;
import club.p6e.cloud.console.domain.aggregate.PermissionUrlGroupListAggregate;
import club.p6e.cloud.console.domain.aggregate.PermissionUrlListAggregate;
import club.p6e.cloud.console.domain.entity.PermissionUrlEntity;
import club.p6e.cloud.console.domain.entity.PermissionUrlGroupEntity;
import club.p6e.cloud.console.infrastructure.context.PermissionContext;
import club.p6e.cloud.console.infrastructure.model.PermissionUrlGroupModel;
import club.p6e.cloud.console.infrastructure.model.PermissionUrlModel;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class PermissionServerImpl implements PermissionServer {

    @Override
    public PermissionContext.Url.ListDto urlList(PermissionContext.Url.Request request) {
        final PermissionUrlListAggregate aggregate = PermissionUrlListAggregate.search(
                request.getPage(), request.getSize(),
                request.getQuery(), request.getSort(), request.getSearch()
        );
        final PermissionContext.Url.ListDto result = new PermissionContext.Url.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setList(CopyUtil.runList(aggregate.getList(), PermissionContext.Url.Item.class));
        return result;
    }

    @Override
    public PermissionContext.Url.Dto urlCreate(PermissionContext.Url.Request request) {
        return CopyUtil.run(PermissionUrlEntity.create(
                CopyUtil.run(request, PermissionUrlModel.class)
        ).getModel(), PermissionContext.Url.Dto.class);
    }

    @Override
    public PermissionContext.Url.Dto urlUpdate(PermissionContext.Url.Request request) {
        return CopyUtil.run(PermissionUrlEntity.findById(request.getId()).update(
                CopyUtil.run(request, PermissionUrlModel.class)
        ).getModel(), PermissionContext.Url.Dto.class);
    }

    @Override
    public PermissionContext.Url.Dto urlGet(PermissionContext.Url.Request request) {
        return CopyUtil.run(PermissionUrlEntity.findById(request.getId()).getModel(), PermissionContext.Url.Dto.class);
    }

    @Override
    public PermissionContext.Url.Dto urlDelete(PermissionContext.Url.Request request) {
        return CopyUtil.run(PermissionUrlEntity.findById(
                request.getId()
        ).delete().getModel(), PermissionContext.Url.Dto.class);
    }

    @Override
    public PermissionContext.UrlGroup.ListDto urlGroupList(PermissionContext.UrlGroup.Request request) {
        final PermissionUrlGroupListAggregate aggregate = PermissionUrlGroupListAggregate.search(
                request.getPage(),
                request.getSize(),
                request.getQuery(),
                request.getSort(),
                request.getSearch()
        );
        final PermissionContext.UrlGroup.ListDto result = new PermissionContext.UrlGroup.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setList(CopyUtil.runList(aggregate.getList(), PermissionContext.UrlGroup.Item.class));
        return result;
    }

    @Override
    public PermissionContext.UrlGroup.Dto urlGroupCreate(PermissionContext.UrlGroup.Request request) {
        return CopyUtil.run(PermissionUrlGroupEntity.create(
                CopyUtil.run(request, PermissionUrlGroupModel.class)
        ).getModel(), PermissionContext.UrlGroup.Dto.class);
    }

    @Override
    public PermissionContext.UrlGroup.Dto urlGroupUpdate(PermissionContext.UrlGroup.Request request) {
        return CopyUtil.run(PermissionUrlGroupEntity.findById(request.getId()).update(
                CopyUtil.run(request, PermissionUrlGroupModel.class)
        ).getModel(), PermissionContext.UrlGroup.Dto.class);
    }

    @Override
    public PermissionContext.UrlGroup.Dto urlGroupGet(PermissionContext.UrlGroup.Request request) {
        return CopyUtil.run(
                PermissionUrlGroupEntity.findById(request.getId()).getModel(),
                PermissionContext.UrlGroup.Dto.class
        );
    }

    @Override
    public PermissionContext.UrlGroup.Dto urlGroupDelete(PermissionContext.UrlGroup.Request request) {
        return CopyUtil.run(
                PermissionUrlGroupEntity.findById(request.getId()).delete().getModel(),
                PermissionContext.UrlGroup.Dto.class
        );
    }

    @Override
    public PermissionContext.Url.Dto urlDetails(PermissionContext.Url.Request request) {
        return null;
    }

    @Override
    public PermissionContext.UrlGroup.Details.Dto urlGroupDetails(PermissionContext.UrlGroup.Details.Request request) {
        final PermissionUrlGroupDetailsAggregate aggregate =
                PermissionUrlGroupDetailsAggregate.get(request.getId(), request.getPage(), request.getSize());
        final PermissionContext.UrlGroup.Details.Dto result =
                CopyUtil.run(aggregate.getModel(), PermissionContext.UrlGroup.Details.Dto.class);
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setUrls(CopyUtil.runList(aggregate.getList(), PermissionContext.UrlGroup.Details.Url.class));
        return result;
    }

}
