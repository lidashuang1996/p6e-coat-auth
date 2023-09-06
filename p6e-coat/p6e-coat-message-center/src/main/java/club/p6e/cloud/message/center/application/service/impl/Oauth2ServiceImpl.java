package club.p6e.cloud.message.center.application.service.impl;

import club.p6e.cloud.message.center.application.service.Oauth2Service;
import club.p6e.cloud.console.domain.aggregate.Oauth2ClientListAggregate;
import club.p6e.cloud.console.domain.entity.Oauth2ClientEntity;
import club.p6e.cloud.message.center.infrastructure.context.Oauth2Context;
import club.p6e.cloud.console.infrastructure.model.Oauth2ClientModel;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class Oauth2ServiceImpl implements Oauth2Service {

    @Override
    public Oauth2Context.Client.ListDto client(Oauth2Context.Client.Request request) {
        final Oauth2ClientListAggregate aggregate = Oauth2ClientListAggregate.search(
                request.getPage(),
                request.getSize(),
                request.getQuery(),
                request.getSort(),
                request.getSearch()
        );
        final Oauth2Context.Client.ListDto result = new Oauth2Context.Client.ListDto();
        result.setPage(aggregate.getPage());
        result.setSize(aggregate.getSize());
        result.setTotal(aggregate.getTotal());
        result.setList(CopyUtil.runList(aggregate.getList(), Oauth2Context.Client.Item.class));
        return result;
    }

    @Override
    public Oauth2Context.Client.Dto getClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity.findById(request.getId());
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

    @Override
    public Oauth2Context.Client.Dto createClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity
                .create(CopyUtil.run(request, Oauth2ClientModel.class));
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

    @Override
    public Oauth2Context.Client.Dto updateClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity
                .findById(request.getId())
                .update(CopyUtil.run(request, Oauth2ClientModel.class));
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

    @Override
    public Oauth2Context.Client.Dto deleteClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity.findById(request.getId()).delete();
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

}
