package club.p6e.coat.gateway.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author lidashuang
 * @version 1.0
 */
@Aspect
@Component
public class AuthCertificateAspect {

    private final AuthCertificateAuthority ca;

    public AuthCertificateAspect(AuthCertificateAuthority ca) {
        this.ca = ca;
    }

    @Pointcut("@annotation(club.p6e.coat.gateway.auth.AuthCertificate))")
    public void pointcut() {
    }

    @Around("pointcut()")
    @SuppressWarnings("ALL")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        final Object r = joinPoint.proceed();
        if (r instanceof Publisher) {
            if (r instanceof Mono) {
                return ((Mono<?>) r).flatMap(v -> {
                    if (v instanceof final AuthUser user) {
                        return execute(user);
                    } else {
                        return Mono.just(v);
                    }
                });
            }
            if (r instanceof Flux) {
                return ((Flux<?>) r).flatMap(v -> {
                    if (v instanceof final AuthUser user) {
                        return execute(user);
                    } else {
                        return Mono.just(v);
                    }
                });
            }
            return r;
        } else {
            return r;
        }
    }

    /**
     * 执行认证
     *
     * @param user
     * @return
     */
    protected Mono<Object> execute(AuthUser user) {
        return ca.issue(user);
    }

}
