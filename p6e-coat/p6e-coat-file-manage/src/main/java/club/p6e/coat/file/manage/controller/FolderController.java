package club.p6e.coat.file.manage.controller;

import club.p6e.coat.file.manage.Properties;
import club.p6e.coat.file.manage.infrastructure.context.FolderContext;
import club.p6e.coat.file.manage.infrastructure.error.GlobalExceptionContext;
import club.p6e.coat.file.manage.service.FolderService;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import com.darvi.hksi.badminton.lib.utils.FileUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/fm")
public class FolderController {

    private final FolderService service;
    private final Properties properties;

    public FolderController(FolderService service, Properties properties) {
        this.service = service;
        this.properties = properties;
    }

    @GetMapping("/root")
    public void root() {

    }

    @GetMapping("/children")
    public void children() {

    }

    @PostMapping("/create/file/{id}")
    public ResultContext createFile(Integer pid,
                                    @PathVariable Integer id,
                                    @RequestBody FolderContext.CreateFileRequest param) {
        if (param == null
                || pid == null
                || param.getName() == null
                || param.getPath() == null) {
            throw GlobalExceptionContext.executeParameterException(this.getClass(), "");
        }
        param.setId(id);
        param.setPid(pid);
        final String path = param.getPath();
        if (!FileUtil.checkFileExist(
                FileUtil.composePath(properties.getUploadBasePath(), path))) {
            throw GlobalExceptionContext.executeParameterException(this.getClass(), "");
        }
        final FolderContext.CreateFileDto result = service.createFile(param);
        return ResultContext.build(CopyUtil.run(result, FolderContext.CreateFileVo.class));
    }

    @PostMapping("/create/folder/{id}")
    public ResultContext createFolder(Integer pid,
                                      @PathVariable Integer id,
                                      @RequestBody FolderContext.CreateFolderRequest param) {
        if (param == null
                || pid == null
                || param.getName() == null) {
            throw GlobalExceptionContext.executeParameterException(this.getClass(), "");
        }
        param.setId(id);
        param.setPid(pid);
        final FolderContext.CreateFolderDto result = service.createFolder(param);
        return ResultContext.build(CopyUtil.run(result, FolderContext.CreateFolderVo.class));
    }

    @PutMapping("/update/{id}")
    public void update(@PathVariable Integer id) {
    }

    @DeleteMapping("/delete/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final FolderContext.DeleteFolderDto result =
                service.delete(new FolderContext.DeleteFolderRequest().setId(id));
        return ResultContext.build(CopyUtil.run(result, FolderContext.DeleteFolderVo.class));
    }

}
