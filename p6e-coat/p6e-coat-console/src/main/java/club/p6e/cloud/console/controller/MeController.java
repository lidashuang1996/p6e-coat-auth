package club.p6e.cloud.console.controller;

import club.p6e.cloud.console.application.service.MeService;
import club.p6e.cloud.console.infrastructure.context.MeContext;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/me")
public class MeController {

    private final MeService server;

    public MeController(MeService server) {
        this.server = server;
    }

    @GetMapping("/info")
    public ResultContext info() {
        final MeContext.Info.Dto result = server.info();
        return ResultContext.build(CopyUtil.run(result, MeContext.Info.Vo.class));
    }

    @PutMapping("/change/password")
    public ResultContext changePassword(@RequestBody MeContext.ChangePassword.Request request) {
        final MeContext.ChangePassword.Dto result = server.changePassword(request);
        return ResultContext.build(CopyUtil.run(result, MeContext.ChangePassword.Vo.class));
    }

}
