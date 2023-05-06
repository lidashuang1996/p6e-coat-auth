package club.p6e.coat.gateway.auth;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
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
    public AuthForeignMinistryVisaTemplate verificationAccessToken(ServerHttpRequest request);

    /**
     * 验证签证
     *
     * @param request HttpServletRequest 请求对象
     * @return ForeignMinistryVisaTemplate 签证模板对象
     */
    public AuthForeignMinistryVisaTemplate verificationRefreshToken(ServerHttpRequest request);

    /**
     * 刷新签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @return 结果对象
     */
    public Object refresh(ServerHttpRequest request, ServerHttpResponse response);

    /**
     * 删除签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @return ForeignMinistryVisaTemplate 签证模板对象
     */
    public AuthForeignMinistryVisaTemplate delete(ServerHttpRequest request, ServerHttpResponse response);

    /**
     * 生成签证
     *
     * @param request  HttpServletRequest 请求对象
     * @param response HttpServletResponse 结果对象
     * @param template ForeignMinistryVisaTemplate 签证模板对象
     * @return 结果对象
     */
    public Mono<Object> apply(ServerHttpRequest request, ServerHttpResponse response, AuthForeignMinistryVisaTemplate template);


}
