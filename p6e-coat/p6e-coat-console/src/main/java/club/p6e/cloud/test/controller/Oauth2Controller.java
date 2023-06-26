package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.DictionaryService;
import club.p6e.cloud.test.application.service.Oauth2Service;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.context.Oauth2Context;
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

    private final Oauth2Service server;

    public Oauth2Controller(Oauth2Service server) {
        this.server = server;
    }

    @GetMapping("/client")
    public ResultContext clientList(Oauth2Context.Client.Request request) {
        return getClientList(request);
    }

    @GetMapping("/client/list")
    public ResultContext getClientList(Oauth2Context.Client.Request request) {
        return postClientList(request);
    }

    @PostMapping("/client/list")
    public ResultContext postClientList(@RequestBody Oauth2Context.Client.Request request) {
        final Oauth2Context.Client.ListDto result = server.list(request);
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.ListVo.class));
    }

//    @PostMapping("/client")
//    public ResultContext create(@RequestBody Oauth2Context.Request request) {
//        if (request == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun create(Oauth2Context.Request request)",
//                    "Request parameter exception."
//            );
//        }
//        if (request.getType() == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun create(Oauth2Context.Request request)",
//                    "Request parameter [type] exception."
//            );
//        }
//        if (request.getKey() == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun create(Oauth2Context.Request request)",
//                    "Request parameter [key] exception."
//            );
//        }
//        if (request.getValue() == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun create(Oauth2Context.Request request)",
//                    "Request parameter [value] exception."
//            );
//        }
//        if (request.getLanguage() == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun create(Oauth2Context.Request request)",
//                    "Request parameter [language] exception."
//            );
//        }
//        final Oauth2Context.Dto result = server.create(request);
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Vo.class));
//    }
//
//    @PutMapping("/client/{id}")
//    public ResultContext update(@PathVariable Integer id, @RequestBody Oauth2Context.Request request) {
//        if (request == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun update(Integer id, Oauth2Context.Request request)",
//                    "Request parameter exception."
//            );
//        }
//        if (request.getType() == null
//                && request.getKey() == null
//                && request.getValue() == null
//                && request.getLanguage() == null) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun update(Integer id, Oauth2Context.Request request)",
//                    "Request parameter exception."
//            );
//        }
//        final Oauth2Context.Dto result = server.update(request.setId(id));
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Vo.class));
//    }
//
//    @GetMapping("/client/{id}")
//    public ResultContext get(@PathVariable Integer id) {
//        final Oauth2Context.Dto result = server.get(new Oauth2Context.Request().setId(id));
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Vo.class));
//    }
//
//    @DeleteMapping("/client/{id}")
//    public ResultContext delete(@PathVariable Integer id) {
//        final Oauth2Context.Dto result = server.delete(new Oauth2Context.Request().setId(id));
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Vo.class));
//    }
//
//
//    @GetMapping("/authorize")
//    public ResultContext authorizeList(Oauth2Context.Request request) {
//        return getAuthorizeList(request);
//    }
//
//    @GetMapping("/authorize/list")
//    public ResultContext getAuthorizeList(Oauth2Context.Request request) {
//        return postAuthorizeList(request);
//    }
//
//    @PostMapping("/authorize/list")
//    public ResultContext postAuthorizeList(@RequestBody Oauth2Context.Request request) {
//        if (request != null
//                && request.getSort() != null
//                && !request.getSort().validation(DictionaryModel.class)) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun list(Oauth2Context.Request request)",
//                    "Request sort validation exception."
//            );
//        }
//        if (request != null
//                && request.getSearch() != null
//                && !request.getSearch().validation(DictionaryModel.class)) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun list(Oauth2Context.Request request)",
//                    "Request search validation exception."
//            );
//        }
//        final Oauth2Context.ListDto result = server.list(request);
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.ListVo.class));
//    }
//
//    @GetMapping("/log")
//    public ResultContext logList(Oauth2Context.Request request) {
//        return getLogList(request);
//    }
//
//    @GetMapping("/log/list")
//    public ResultContext getLogList(Oauth2Context.Request request) {
//        return postLogList(request);
//    }
//
//    @PostMapping("/log/list")
//    public ResultContext postLogList(@RequestBody Oauth2Context.Request request) {
//        if (request != null
//                && request.getSort() != null
//                && !request.getSort().validation(DictionaryModel.class)) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun list(Oauth2Context.Request request)",
//                    "Request sort validation exception."
//            );
//        }
//        if (request != null
//                && request.getSearch() != null
//                && !request.getSearch().validation(DictionaryModel.class)) {
//            throw GlobalExceptionContext.executeParameterException(
//                    this.getClass(),
//                    "fun list(Oauth2Context.Request request)",
//                    "Request search validation exception."
//            );
//        }
//        final Oauth2Context.ListDto result = server.list(request);
//        return ResultContext.build(CopyUtil.run(result, Oauth2Context.ListVo.class));
//    }

}
