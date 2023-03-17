package club.p6e.coat.file.service;

import club.p6e.coat.file.context.SimpleUploadContext;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface SimpleUploadService {
    Mono<Map<String, Object>> execute(SimpleUploadContext context);
}
