package club.p6e.coat.gateway.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Aspect
@Component
public class AuthOauth2ClientAspect {

    private final AuthOauth2ClientInterceptor interceptor;

    public AuthOauth2ClientAspect(AuthOauth2ClientInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    @Pointcut("@annotation(club.p6e.coat.gateway.auth.AuthOauth2Client))")
    public void pointcut() {
    }

    @SuppressWarnings("ALL")
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return interceptor
                .execute(getServerWebExchange(joinPoint))
                .flatMap(e -> {
                    final Object r;
                    try {
                        r = joinPoint.proceed();
                    } catch (Throwable throwable) {
                        return Mono.error(new Throwable(throwable));
                    }
                    if (r instanceof Publisher) {
                        if (r instanceof Mono) {
                            return ((Mono<?>) r);
                        }
                        if (r instanceof Flux) {
                            return ((Flux<?>) r)
                                    .collectList()
                                    .map(l -> Mono.just(l));
                        }
                        return Mono.just(r);
                    } else {
                        return Mono.just(r);
                    }
                });
    }

    protected ServerWebExchange getServerWebExchange(ProceedingJoinPoint joinPoint) {
        final Object[] args = joinPoint.getArgs();
        if (args != null) {
            for (final Object arg : args) {
                if (arg instanceof ServerWebExchange) {
                    return (ServerWebExchange) arg;
                }
            }
        }
        throw new RuntimeException();
    }

}
