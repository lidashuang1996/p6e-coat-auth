package club.p6e.coat.file.aspect;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 切面（钩子）的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class DefaultAspectImpl implements
        Aspect,
        CloseUploadAspect,
        // 下载操作的切面（钩子）的默认实现
        DownloadAspect {

    @Override
    public Mono<Boolean> before(Map<String, Object> data) {
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> after(Map<String, Object> data, Map<String, Object> result) {
        return Mono.just(true);
    }

}
