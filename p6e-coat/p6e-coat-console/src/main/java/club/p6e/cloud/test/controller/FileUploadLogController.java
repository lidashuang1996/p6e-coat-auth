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
@RequestMapping("/file/upload/log")
public class FileUploadLogController {

    private final DictionaryService server;

    public FileUploadLogController(DictionaryService server) {
        this.server = server;
    }

    @GetMapping
    public ResultContext list(DictionaryContext.Request request) {
        return getList(request);
    }

    @GetMapping("/list")
    public ResultContext getList(DictionaryContext.Request request) {
        return postList(request);
    }

    @PostMapping("/list")
    public ResultContext postList(@RequestBody DictionaryContext.Request request) {
        if (request != null
                && request.getSort() != null
                && !request.getSort().validation(DictionaryModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(DictionaryContext.Request request)",
                    "Request sort validation exception."
            );
        }
        if (request != null
                && request.getSearch() != null
                && !request.getSearch().validation(DictionaryModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(DictionaryContext.Request request)",
                    "Request search validation exception."
            );
        }
        final DictionaryContext.ListDto result = server.list(request);
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.ListVo.class));
    }

}
