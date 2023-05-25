package club.p6e.coat.gateway.auth.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * OAUTH2 TOKEN
 *
 * @param <P> 参数对象类型
 * @param <R> 结果对象类型
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/oauth2/token")
public interface Oauth2TokenController<P, R> {

    /**
     * 条件注册的条件表达式
     */
    public final static String CONDITIONAL_EXPRESSION = "#{${p6e.auth.oauth2.enable:false}}";

    /**
     * TOKEN
     *
     * @param param 请求对象
     * @return 结果对象
     */
    @RequestMapping(value = "", method = {RequestMethod.POST, RequestMethod.GET})
    public R execute(P param);

}
