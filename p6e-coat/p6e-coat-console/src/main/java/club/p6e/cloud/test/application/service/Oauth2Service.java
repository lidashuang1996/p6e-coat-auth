package club.p6e.cloud.test.application.service;

import club.p6e.cloud.test.infrastructure.context.Oauth2Context;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2Service {

    Oauth2Context.Client.ListDto client(Oauth2Context.Client.Request request);

    Oauth2Context.Client.Dto getClient(Oauth2Context.Client.Request request);

    Oauth2Context.Client.Dto createClient(Oauth2Context.Client.Request request);

    Oauth2Context.Client.Dto updateClient(Oauth2Context.Client.Request request);

    Oauth2Context.Client.Dto deleteClient(Oauth2Context.Client.Request request);
}
