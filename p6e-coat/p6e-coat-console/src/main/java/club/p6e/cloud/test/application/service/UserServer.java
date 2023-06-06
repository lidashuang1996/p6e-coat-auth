package club.p6e.cloud.test.application.service;

import club.p6e.cloud.test.infrastructure.context.UserContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface UserServer {

    UserContext.ListDto list(UserContext.Request request);

    UserContext.Dto get(UserContext.Request request);

    UserContext.Dto create(UserContext.Request request);

    UserContext.Dto update(UserContext.Request request);

    UserContext.Dto delete(UserContext.Request request);

}
