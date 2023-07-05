package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.Oauth2Service;
import club.p6e.cloud.test.infrastructure.context.Oauth2Context;
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
        final Oauth2Context.Client.ListDto result = server.client(request);
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.ListVo.class));
    }

    @PostMapping("/client")
    public ResultContext create(@RequestBody Oauth2Context.Client.Request request) {
        final Oauth2Context.Client.Dto result = server.createClient(request);
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.Vo.class));
    }

    @GetMapping("/client/{id}")
    public ResultContext get(@PathVariable Integer id) {
        final Oauth2Context.Client.Dto result = server.getClient(new Oauth2Context.Client.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.Vo.class));
    }

    @PutMapping("/client/{id}")
    public ResultContext update(@PathVariable Integer id, @RequestBody Oauth2Context.Client.Request request) {
        final Oauth2Context.Client.Dto result = server.updateClient(request.setId(id));
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.Vo.class));
    }

    @DeleteMapping("/client/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final Oauth2Context.Client.Dto result = server.deleteClient(new Oauth2Context.Client.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, Oauth2Context.Client.Vo.class));
    }

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
