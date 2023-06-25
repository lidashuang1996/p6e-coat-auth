package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.DictionaryService;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.context.DictionaryContext;
import club.p6e.cloud.test.infrastructure.model.DictionaryModel;
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

    private final DictionaryService server;

    public MeController(DictionaryService server) {
        this.server = server;
    }

    @GetMapping("/info")
    public ResultContext info(DictionaryContext.Request request) {
        return ResultContext.build();
    }

    @GetMapping("/change/password")
    public ResultContext changePassword(DictionaryContext.Request request) {
        return ResultContext.build();
    }

}
