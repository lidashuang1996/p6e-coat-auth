package club.p6e.coat.gateway.permission;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * 权限验证器默认的实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = PermissionValidator.class,
        ignored = PermissionValidatorImpl.class
)
public class PermissionValidatorImpl implements PermissionValidator {

    /**
     * 验证是否具备权限
     *
     * @param url    请求的路径
     * @param method 请求的方法
     * @param user   请求的用户信息
     * @return Mono/PermissionDetails 权限信息对象
     */
    @Override
    @SuppressWarnings("ALL")
    public Mono<PermissionDetails> execute(String url, String method, Map<String, Object> user) {
        if (user != null && user.get("permissionGroups") instanceof final List<?> groups) {
            final PermissionDetails details = PermissionCore.execute(url, method, (List<String>) groups);
            return details == null ? Mono.empty() : Mono.just(details);
        } else {
            return Mono.empty();
        }
    }

}
