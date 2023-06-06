package club.p6e.cloud.test.controller;

import club.p6e.cloud.test.application.service.UserServer;
import club.p6e.cloud.test.error.GlobalExceptionContext;
import club.p6e.cloud.test.infrastructure.context.UserContext;
import club.p6e.cloud.test.infrastructure.model.UserModel;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import com.darvi.hksi.badminton.lib.utils.CopyUtil;
import org.springframework.web.bind.annotation.*;

/**
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServer server;

    public UserController(UserServer server) {
        this.server = server;
    }

    @GetMapping()
    public ResultContext list(UserContext.Request request) {
        return getList(request);
    }

    @GetMapping("/list")
    public ResultContext getList(UserContext.Request request) {
        return postList(request);
    }

    @PostMapping("/list")
    public ResultContext postList(@RequestBody UserContext.Request request) {
        if (request != null
                && request.getSort() != null
                && !request.getSort().validation(UserModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(UserContext.Request request)",
                    "Request sort validation exception."
            );
        }
        if (request != null
                && request.getSearch() != null
                && !request.getSearch().validation(UserModel.class)) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun list(UserContext.Request request)",
                    "Request search validation exception."
            );
        }
        final UserContext.ListDto result = server.list(request);
        return ResultContext.build(CopyUtil.run(result, UserContext.ListVo.class));
    }

    @PostMapping()
    public ResultContext create(@RequestBody UserContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(UserContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getAccount() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(UserContext.Request request)",
                    "Request parameter [account] exception."
            );
        }
        if (request.getPassword() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun create(UserContext.Request request)",
                    "Request parameter [password] exception."
            );
        }
        final UserContext.Dto result = server.create(request);
        return ResultContext.build(CopyUtil.run(result, UserContext.Vo.class));
    }

    @PutMapping("/{id}")
    public ResultContext update(@PathVariable Integer id, @RequestBody UserContext.Request request) {
        if (request == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, UserContext.Request request)",
                    "Request parameter exception."
            );
        }
        if (request.getStatus() == null
                && request.getEnabled() == null
                && request.getName() == null
                && request.getNickname() == null
                && request.getAvatar() == null
                && request.getDescribe() == null) {
            throw GlobalExceptionContext.executeParameterException(
                    this.getClass(),
                    "fun update(Integer id, UserContext.Request request)",
                    "Request parameter exception."
            );
        }
        final UserContext.Dto result = server.update(request.setId(id));
        return ResultContext.build(CopyUtil.run(result, UserContext.Vo.class));
    }

    @GetMapping("/{id}")
    public ResultContext get(@PathVariable Integer id) {
        final UserContext.Dto result = server.get(new UserContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, UserContext.Vo.class));
    }

    @DeleteMapping("/{id}")
    public ResultContext delete(@PathVariable Integer id) {
        final UserContext.Dto result = server.delete(new UserContext.Request().setId(id));
        return ResultContext.build(CopyUtil.run(result, UserContext.Vo.class));
    }

}
