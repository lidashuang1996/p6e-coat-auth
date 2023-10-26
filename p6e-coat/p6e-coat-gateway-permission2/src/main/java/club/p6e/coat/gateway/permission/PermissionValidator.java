package club.p6e.coat.gateway.permission;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 权限验证器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface PermissionValidator {

    /**
     * 验证是否具备权限
     *
     * @param url    请求的路径
     * @param method 请求的方法
     * @param user   请求的用户信息
     * @return Mono/PermissionDetails 权限信息对象
     */
    public Mono<PermissionDetails> execute(String url, String method, Map<String, Object> user);

}
