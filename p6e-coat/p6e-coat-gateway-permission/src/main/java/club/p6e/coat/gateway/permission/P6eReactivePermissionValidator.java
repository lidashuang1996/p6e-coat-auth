package club.p6e.coat.gateway.permission;

import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class P6eReactivePermissionValidator implements ReactivePermissionValidator {


    @Override
    public Mono<PermissionDetails> execute(String url, String method, Map<String, Object> user) {
        return null;
    }
}
