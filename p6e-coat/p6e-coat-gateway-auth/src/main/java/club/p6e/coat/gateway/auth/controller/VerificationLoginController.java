package club.p6e.coat.gateway.auth.controller;

import club.p6e.coat.gateway.auth.AuthUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 验证登录
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/login/verification")
public interface VerificationLoginController<P, R extends AuthUser> {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.login.enable:false}}";

    /**
     * [ POST ]
     * JSON/BODY
     * 验证登录的操作
     *
     * @param param 请求对象
     * @return 结果对象
     */
    @PostMapping()
    public Mono<R> execute(@RequestBody P param);

}
