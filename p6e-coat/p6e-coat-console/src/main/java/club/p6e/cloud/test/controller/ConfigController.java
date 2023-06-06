package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.ConfigService;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.context.ConfigContext;
import club.p6e.cloud.test.infrastructure.model.ConfigModel;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ConfigService server;

    public ConfigController(ConfigService server) {
        this.server = server;
    }

    @GetMapping()
    public ResultContext list(ConfigContext.Request request) {
        return getList(request);
    }

    @GetMapping("/list")
    public ResultContext getList(ConfigContext.Request request) {
        return postList(request);
    }

    @PostMapping("/list")
    public ResultContext postList(@RequestBody ConfigContext.Request request) {
        if (request != null
                && request.getSort() != null
                && !request.getSort().validation(ConfigModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(ConfigContext.Request request)",
                    "Request sort validation exception."
            );
        }
        if (request != null
                && request.getSearch() != null
                && !request.getSearch().validation(ConfigModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(ConfigContext.Request request)",
                    "Request search validation exception."
            );
        }
        final ConfigContext.ListDto result = server.list(request);
        return ResultContext.build(CopyUtil.run(result, ConfigContext.ListVo.class));
    }

    @PostMapping()
    public ResultContext create(@RequestBody ConfigContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(ConfigContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getKey() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(ConfigContext.Request request)",
                    "Request parameter [key] exception."
            );
        }
        if (request.getValue() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(ConfigContext.Request request)",
                    "Request parameter [value] exception."
            );
        }
        final ConfigContext.Dto result = server.create(request);
        return ResultContext.build(CopyUtil.run(result, ConfigContext.Vo.class));
    }

    @PutMapping("/{id}")
    public ResultContext update(@PathVariable Integer id, @RequestBody ConfigContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, ConfigContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getKey() == null && request.getValue() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, ConfigContext.Request request)",
                    "Request parameter exception."
            );
        }
        final ConfigContext.Dto result = server.update(request.setId(id));
        return ResultContext.build(CopyUtil.run(result, ConfigContext.Vo.class));
    }

    @GetMapping("/{id}")
    public ResultContext get(@PathVariable Integer id) {
        final ConfigContext.Dto result = server.get(new ConfigContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, ConfigContext.Vo.class));
    }

    @DeleteMapping("/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final ConfigContext.Dto result = server.delete(new ConfigContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, ConfigContext.Vo.class));
    }
    
}
