package club.p6e.coat.gateway.auth.cache.memory;

import club.p6e.coat.gateway.auth.P6eConditionalExpression;
import club.p6e.coat.gateway.auth.cache.AccountPasswordLoginSignatureCache;
import club.p6e.coat.gateway.auth.cache.memory.support.MemoryCache;
import club.p6e.coat.gateway.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
@ConditionalOnMissingBean(
        value = AccountPasswordLoginSignatureCache.class,
        ignored = AccountPasswordLoginSignatureMemoryCache.class
)
@ConditionalOnExpression(value = P6eConditionalExpression.CONDITIONAL_EXPRESSION)
public class AccountPasswordLoginSignatureMemoryCache
        extends MemoryCache implements AccountPasswordLoginSignatureCache {

    private final ReactiveMemoryTemplate template;

    public AccountPasswordLoginSignatureMemoryCache(ReactiveMemoryTemplate template) {
        this.template = template;
    }

    @Override
    public Mono<Long> del(String key) {
        return Mono.just(template.del(CACHE_PREFIX + key));
    }

    @Override
    public Mono<String> get(String key) {
        final String r = template.get(CACHE_PREFIX + key, String.class);
        return r == null ? Mono.empty() : Mono.just(r);
    }

    @Override
    public Mono<Boolean> set(String key, String value) {
        return Mono.just(template.set(CACHE_PREFIX + key, value, EXPIRATION_TIME));
    }

}
