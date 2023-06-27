package club.p6e.cloud.test.application.service.impl;

import club.p6e.cloud.test.application.service.Oauth2Service;
import club.p6e.cloud.test.domain.aggregate.Oauth2ClientListAggregate;
import club.p6e.cloud.test.domain.entity.Oauth2ClientEntity;
import club.p6e.cloud.test.infrastructure.context.Oauth2Context;
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
        final Oauth2ClientEntity entity = Oauth2ClientEntity.create(request);
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

    @Override
    public Oauth2Context.Client.Dto updateClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity.findById(request.getId()).update(request);
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

    @Override
    public Oauth2Context.Client.Dto deleteClient(Oauth2Context.Client.Request request) {
        final Oauth2ClientEntity entity = Oauth2ClientEntity.findById(request.getId()).delete(request);
        return CopyUtil.run(entity.getModel(), Oauth2Context.Client.Dto.class);
    }

}
