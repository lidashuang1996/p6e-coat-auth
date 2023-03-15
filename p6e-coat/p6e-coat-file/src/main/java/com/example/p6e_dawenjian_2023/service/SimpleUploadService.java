package com.example.p6e_dawenjian_2023.service;

import com.example.p6e_dawenjian_2023.context.SimpleUploadContext;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface SimpleUploadService {
    Mono<Map<String, Object>> execute(SimpleUploadContext context);
}
