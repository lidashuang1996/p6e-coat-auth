package club.p6e.cloud.console.controller;

import club.p6e.cloud.console.application.service.FileUploadLogService;
import club.p6e.cloud.console.infrastructure.context.FileUploadLogContext;
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

    private final FileUploadLogService server;

    public FileUploadLogController(FileUploadLogService server) {
        this.server = server;
    }

    @GetMapping
    public ResultContext list(FileUploadLogContext.Request request) {
        return getList(request);
    }

    @GetMapping("/list")
    public ResultContext getList(FileUploadLogContext.Request request) {
        return postList(request);
    }

    @PostMapping("/list")
    public ResultContext postList(@RequestBody FileUploadLogContext.Request request) {
        final FileUploadLogContext.ListDto result = server.list(request);
        return ResultContext.build(CopyUtil.run(result, FileUploadLogContext.ListVo.class));
    }

    @GetMapping("/details/{id}")
    public ResultContext details(@PathVariable Integer id) {
        final FileUploadLogContext.Details.Dto result =
                server.details(new FileUploadLogContext.Details.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, FileUploadLogContext.Details.Vo.class));
    }

}
