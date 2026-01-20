package club.p6e.coat.auth.client;

import club.p6e.coat.auth.client.cache.AuthStateCache;
import club.p6e.coat.auth.client.cache.AuthStateCacheReactive;
import club.p6e.coat.auth.client.cache.memory.AuthStateMemoryCache;
import club.p6e.coat.auth.client.cache.memory.AuthStateMemoryCacheReactive;
import club.p6e.coat.auth.client.cache.memory.support.MemoryTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthClientMemoryCacheConfig {

    @Bean
    public MemoryTemplate injectionMemoryTemplate() {
        return new MemoryTemplate();
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.web.servlet.package-info")
    public AuthStateCache injectionAuthStateCache(MemoryTemplate template) {
        return new AuthStateMemoryCache(template);
    }

    @Bean
    @ConditionalOnClass(name = "org.springframework.web.reactive.package-info")
    public AuthStateCacheReactive injectionAuthStateCacheReactive(MemoryTemplate template) {
        return new AuthStateMemoryCacheReactive(template);
    }

}
