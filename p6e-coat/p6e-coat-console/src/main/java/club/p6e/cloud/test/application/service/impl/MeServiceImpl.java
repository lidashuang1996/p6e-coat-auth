package club.p6e.cloud.test.application.service.impl;

import club.p6e.cloud.test.application.service.MeService;
import club.p6e.cloud.test.domain.entity.UserAuthEntity;
import club.p6e.cloud.test.infrastructure.context.MeContext;
import club.p6e.cloud.test.infrastructure.model.UserAuthModel;
import com.darvi.hksi.badminton.lib.AuthCore;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.stereotype.Service;

/**
 * @author lidashuang
 * @version 1.0
 */
@Service
public class MeServiceImpl implements MeService {

    @Override
    public MeContext.Info.Dto info() {
        return CopyUtil.run(AuthCore.getThreadInstance(), MeContext.Info.Dto.class);
    }

    @Override
    public MeContext.ChangePassword.Dto changePassword(MeContext.ChangePassword.Request request) {
        final AuthCore core = AuthCore.getThreadInstance();
        final UserAuthEntity entity = UserAuthEntity.findById(core.getId());
        if (entity.verifyPassword(request.getOldPassword())) {
            return CopyUtil.run(
                    entity.update(new UserAuthModel().setPassword(request.getNewPassword())).getModel(),
                    MeContext.ChangePassword.Dto.class
            );
        }
        return new MeContext.ChangePassword.Dto().setContent("SUCCESS");
    }

}
