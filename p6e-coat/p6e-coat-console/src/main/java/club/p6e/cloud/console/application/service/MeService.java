package club.p6e.cloud.console.application.service;

import club.p6e.cloud.console.infrastructure.context.MeContext;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface MeService {

    MeContext.Info.Dto info();

    MeContext.ChangePassword.Dto changePassword(MeContext.ChangePassword.Request request);

}
