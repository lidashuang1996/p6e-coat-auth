package club.p6e.coat.gateway.auth;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthForeignMinistry {

    /**
     * 验证签证
     *
     * @param request HttpServletRequest 请求对象
     * @return ForeignMinistryVisaTemplate 签证模板对象
     */
    public AuthForeignMinistryVisaTemplate verificationAccessToken(ServerRequest request);

    /**
     * 验证签证
     *
     * @param request HttpServletRequest 请求对象
     * @return ForeignMinistryVisaTemplate 签证模板对象
     */
    public AuthForeignMinistryVisaTemplate verificationRefreshToken(ServerRequest request);

    /**
     * 刷新签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @return 结果对象
     */
    public Object refresh(ServerRequest request, ServerResponse response);

    /**
     * 删除签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @return ForeignMinistryVisaTemplate 签证模板对象
     */
    public AuthForeignMinistryVisaTemplate delete(ServerRequest request, ServerResponse response);

    /**
     * 生成签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @param template ForeignMinistryVisaTemplate 签证模板对象
     * @return 结果对象
     */
    public Mono<Object> apply(ServerRequest request, ServerResponse response, AuthForeignMinistryVisaTemplate template);


}
