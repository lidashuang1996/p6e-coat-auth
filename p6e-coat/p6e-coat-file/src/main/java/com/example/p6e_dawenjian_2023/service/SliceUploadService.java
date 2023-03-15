package com.example.p6e_dawenjian_2023.service;

import com.example.p6e_dawenjian_2023.context.SliceUploadContext;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 分片上传
 * 步骤1: 打开上传操作
 *
 * @author lidashuang
 * @version 1.0
 */
public interface SliceUploadService {

    /**
     * 执行打开上传操作
     *
     * @param context 打开上传上下文对象
     * @return 结果对象
     */
    public Mono<Map<String, Object>> execute(SliceUploadContext context);

}
