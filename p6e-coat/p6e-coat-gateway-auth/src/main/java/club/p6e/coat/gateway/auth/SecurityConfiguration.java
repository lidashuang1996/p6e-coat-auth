package club.p6e.coat.gateway.auth;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AbstractUserDetailsReactiveAuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authentication.ReactivePreAuthenticatedAuthenticationManager;
import org.springframework.security.web.server.authentication.ServerAuthenticationEntryPointFailureHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain injectingSecurityWebFilterChainBean(ServerHttpSecurity http) {
        return http.authorizeExchange().matchers(new ServerWebExchangeMatcher() {
            @Override
            public Mono<MatchResult> matches(ServerWebExchange exchange) {
                System.out.println(
                        exchange.getRequest().getPath().value()
                );
                return (exchange.getRequest().getPath().value()).startsWith("/test") ? MatchResult.notMatch() : MatchResult.match();
            }
        }).permitAll()
                .anyExchange().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .build();
    }

    @Bean
    public ReactiveAuthenticationManager authenticationWebFil2ter(AuthUserDetailsServiceImpl authUserDetailsService) {
        return new ReactiveAuthenticationManager() {
            @Override
            public Mono<Authentication> authenticate(Authentication authentication) {
                System.out.println("authentication" + authentication);
                String username = authentication.getName();
                String password = authentication.getCredentials().toString();
                System.out.println("password " + password);
                System.out.println("db password " + passwordEncoder().encode(password));
                return authUserDetailsService.findByUsername(username)
                        .filter(userDetails -> passwordEncoder().matches(password, userDetails.getPassword()))
                        .map(userDetails -> new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
            }
        };
    }

    @Bean
    public AuthenticationWebFilter authenticationWebFilter(ReactiveAuthenticationManager manager) {
        AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(manager);
        authenticationWebFilter.setServerAuthenticationConverter(new AuthServerBasicTokenAuthenticationConverter());
        authenticationWebFilter.setAuthenticationFailureHandler(new ServerAuthenticationEntryPointFailureHandler(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED)));
        return authenticationWebFilter;
    }

    @Bean
    public AuthPasswordEncoder passwordEncoder() {
        return new AuthPasswordEncoder();
    }

}

/**
 * Spring Security 提供了一系列的拦截器（interceptor）用于在请求处理过程中进行安全性验证和授权操作。这些拦截器按照特定的顺序执行，以确保安全性规则得到正确地应用。
 * <p>
 * 以下是 Spring Security 拦截器的执行顺序（从前到后）以及它们的作用：
 * <p>
 * ChannelProcessingFilter：用于检查请求是否通过合适的协议通道（例如 HTTP 或 HTTPS）访问。
 * <p>
 * SecurityContextPersistenceFilter：用于将当前用户的安全上下文（SecurityContext）持久化到后端存储或从后端存储加载安全上下文。
 * <p>
 * ConcurrentSessionFilter：用于检查用户是否在多个并发会话中进行了登录，并在必要时阻止用户同时在多个会话中进行访问。
 * <p>
 * LogoutFilter：用于处理用户登出请求。
 * <p>
 * UsernamePasswordAuthenticationFilter：用于处理基于用户名和密码的身份认证请求。
 * <p>
 * DefaultLoginPageGeneratingFilter：用于生成默认的登录页面。
 * <p>
 * DefaultLogoutPageGeneratingFilter：用于生成默认的登出页面。
 * <p>
 * BasicAuthenticationFilter：用于处理基本认证请求。
 * <p>
 * RequestCacheAwareFilter：用于缓存请求，以便在用户完成身份认证后重新执行缓存的请求。
 * <p>
 * SecurityContextHolderAwareRequestFilter：用于在请求中添加安全上下文，以便在请求处理过程中可以方便地访问当前用户的安全信息。
 * <p>
 * RememberMeAuthenticationFilter：用于处理基于"记住我"功能的自动登录请求。
 * <p>
 * AnonymousAuthenticationFilter：用于处理匿名用户的请求。
 * <p>
 * SessionManagementFilter：用于管理用户会话，例如在会话过期或并发登录时进行处理。
 * <p>
 * ExceptionTranslationFilter：用于处理认证和授权过程中的异常。
 * <p>
 * FilterSecurityInterceptor：用于执行基于 URL 和角色的授权操作。
 * <p>
 * 以上仅是 Spring Security 拦截器的一部分，实际上可能会根据配置和使用的功能而有所不同。了解这些拦截器的执行顺序和作用，有助于更好地理解 Spring Security 的请求处理过程，并进行相应的安全性配置。
 */