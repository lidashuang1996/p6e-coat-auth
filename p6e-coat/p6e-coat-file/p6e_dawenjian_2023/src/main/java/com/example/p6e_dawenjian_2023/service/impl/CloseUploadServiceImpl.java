package com.example.p6e_dawenjian_2023.service.impl;

import com.example.p6e_dawenjian_2023.Properties;
import com.example.p6e_dawenjian_2023.context.CloseUploadContext;
import com.example.p6e_dawenjian_2023.model.UploadModel;
import com.example.p6e_dawenjian_2023.repository.UploadRepository;
import com.example.p6e_dawenjian_2023.service.CloseUploadService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class CloseUploadServiceImpl implements CloseUploadService {

    /**
     * 上传存储库对象
     */
    private final UploadRepository repository;

    /**
     * 构造方法初始化
     *
     * @param repository 上传存储库对象
     */
    public CloseUploadServiceImpl(UploadRepository repository) {
        this.repository = repository;
    }


    @Override
    public Mono<Map<String, Object>> execute(CloseUploadContext context) {
        return repository
                .closeLock(context.getId())
                .map(UploadModel::toMap);
    }

}
