package club.p6e.cloud.test.application.service;

import club.p6e.cloud.test.infrastructure.context.Oauth2Context;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Oauth2Service {

    Oauth2Context.Client.ListDto list(Oauth2Context.Client.Request request);

}
