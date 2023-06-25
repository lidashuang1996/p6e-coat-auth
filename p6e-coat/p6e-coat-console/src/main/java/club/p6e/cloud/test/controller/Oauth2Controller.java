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
@RequestMapping("/oauth2")
public class Oauth2Controller {

    private final DictionaryService server;

    public Oauth2Controller(DictionaryService server) {
        this.server = server;
    }

    @GetMapping("/client")
    public ResultContext clientList(DictionaryContext.Request request) {
        return getClientList(request);
    }

    @GetMapping("/client/list")
    public ResultContext getClientList(DictionaryContext.Request request) {
        return postClientList(request);
    }

    @PostMapping("/client/list")
    public ResultContext postClientList(@RequestBody DictionaryContext.Request request) {
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

    @PostMapping("/client")
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

    @PutMapping("/client/{id}")
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

    @GetMapping("/client/{id}")
    public ResultContext get(@PathVariable Integer id) {
        final DictionaryContext.Dto result = server.get(new DictionaryContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }

    @DeleteMapping("/client/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final DictionaryContext.Dto result = server.delete(new DictionaryContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, DictionaryContext.Vo.class));
    }


    @GetMapping("/authorize")
    public ResultContext authorizeList(DictionaryContext.Request request) {
        return getAuthorizeList(request);
    }

    @GetMapping("/authorize/list")
    public ResultContext getAuthorizeList(DictionaryContext.Request request) {
        return postAuthorizeList(request);
    }

    @PostMapping("/authorize/list")
    public ResultContext postAuthorizeList(@RequestBody DictionaryContext.Request request) {
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

    @GetMapping("/log")
    public ResultContext logList(DictionaryContext.Request request) {
        return getLogList(request);
    }

    @GetMapping("/log/list")
    public ResultContext getLogList(DictionaryContext.Request request) {
        return postLogList(request);
    }

    @PostMapping("/log/list")
    public ResultContext postLogList(@RequestBody DictionaryContext.Request request) {
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
