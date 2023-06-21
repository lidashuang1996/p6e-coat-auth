package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.DictionaryService;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.context.DictionaryContext;
import club.p6e.cloud.test.infrastructure.model.DictionaryModel;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private final DictionaryService server;

    public DictionaryController(DictionaryService server) {
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

    @PostMapping
    public ResultContext create(@RequestBody DictionaryContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(DictionaryContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getType() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(DictionaryContext.Request request)",
                    "Request parameter [type] exception."
            );
        }
        if (request.getKey() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(DictionaryContext.Request request)",
                    "Request parameter [key] exception."
            );
        }
        if (request.getValue() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(DictionaryContext.Request request)",
                    "Request parameter [value] exception."
            );
        }
        if (request.getLanguage() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(DictionaryContext.Request request)",
                    "Request parameter [language] exception."
            );
        }
        final DictionaryContext.Dto result = server.create(request);
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }

    @PutMapping("/{id}")
    public ResultContext update(@PathVariable Integer id, @RequestBody DictionaryContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, DictionaryContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getType() == null
                && request.getKey() == null
                && request.getValue() == null
                && request.getLanguage() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, DictionaryContext.Request request)",
                    "Request parameter exception."
            );
        }
        final DictionaryContext.Dto result = server.update(request.setId(id));
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }

    @GetMapping("/{id}")
    public ResultContext get(@PathVariable Integer id) {
        final DictionaryContext.Dto result = server.get(new DictionaryContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }

    @DeleteMapping("/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final DictionaryContext.Dto result = server.delete(new DictionaryContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }

    @GetMapping("/type")
    public ResultContext getType(DictionaryContext.Type.Request request) {
        return postType(request);
    }

    @PostMapping("/type")
    public ResultContext postType(@RequestBody DictionaryContext.Type.Request request) {
        if (request == null || (request.getType() != null && request.getTypes() != null)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun postType(@RequestBody DictionaryContext.Type.Request request)",
                    "Request sort validation exception."
            );
        }
        if (request.getTypes() == null) {
            request.setTypes(new ArrayList<>());
        }
        if (request.getType() != null) {
            request.getTypes().add(request.getType());
        }
        final DictionaryContext.Type.Dto result = server.type(request);
        return ResultContext.build(result.getData());
    }

}
