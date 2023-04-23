package club.p6e.coat.gateway.permission;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface ReactivePermissionValidator {

    /**
     * @param url 请求的 url 地址
     */
    public Mono<PermissionDetails> execute(String url, String method, Map<String, Object> user);

}
