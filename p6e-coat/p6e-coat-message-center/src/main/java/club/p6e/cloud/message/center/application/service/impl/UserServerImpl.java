package club.p6e.cloud.message.center.application.service.impl;

import club.p6e.cloud.message.center.application.service.UserServer;
import club.p6e.cloud.console.domain.aggregate.UserListAggregate;
import club.p6e.cloud.console.domain.entity.UserEntity;
import club.p6e.cloud.message.center.infrastructure.context.UserContext;
import club.p6e.cloud.console.infrastructure.model.UserModel;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class UserServerImpl implements UserServer {

    @Override
    public UserContext.ListDto list(UserContext.Request request) {
        final UserListAggregate aggregate = UserListAggregate.search(
                request.getPage(), request.getSize(),
                request.getQuery(), request.getSort(), request.getSearch()
        );
        final UserContext.ListDto result = new UserContext.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setList(CopyUtil.runList(aggregate.getList(), UserContext.Item.class));
        return result;
    }

    @Override
    public UserContext.Dto get(UserContext.Request request) {
        return CopyUtil.run(UserEntity.findById(request.getId()).getModel(), UserContext.Dto.class);
    }

    @Override
    public UserContext.Dto create(UserContext.Request request) {
        return CopyUtil.run(UserEntity.create(
                CopyUtil.run(request, UserModel.class)
        ).getModel(), UserContext.Dto.class);
    }

    @Override
    public UserContext.Dto update(UserContext.Request request) {
        return CopyUtil.run(UserEntity.findById(request.getId()).update(
                CopyUtil.run(request, UserModel.class)
        ).getModel(), UserContext.Dto.class);
    }

    @Override
    public UserContext.Dto delete(UserContext.Request request) {
        return CopyUtil.run(UserEntity.findById(request.getId()).delete().getModel(), UserContext.Dto.class);
    }

}
