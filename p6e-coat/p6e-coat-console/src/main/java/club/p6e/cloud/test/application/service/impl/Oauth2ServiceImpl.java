package club.p6e.cloud.test.application.service.impl;

import club.p6e.cloud.test.application.service.Oauth2Service;
import club.p6e.cloud.test.infrastructure.context.Oauth2Context;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class Oauth2ServiceImpl implements Oauth2Service {



    @Override
    public Oauth2Context.Client.ListDto list(Oauth2Context.Client.Request request) {
        return null;
    }

}
