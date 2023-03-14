package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.context.OpenUploadContext;
import com.example.p6e_dawenjian_2023.service.SliceUploadService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class SliceUploadServiceImpl implements SliceUploadService {

    @Override
    public Mono<Map<String, Object>> execute(OpenUploadContext context) {
        return null;
    }
}
