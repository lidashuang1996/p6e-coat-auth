package com.example.p6e_dawenjian_2023;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Component
@SuppressWarnings("all")
@ConfigurationProperties(prefix = "hksi.badminton.file")
public class Properties implements Serializable {

    /**
     * 基础的文件路径
     */
    private String path = "/hksi/file/data";

    /**
     * 允许上传的文件大小的最大值
     */
    private long maxSize = 1024 * 1024 * 20;

    /**
     * 允许上传的文件后缀
     */
    private String[] suffixes = new String[]{"chunk"};

    private Map<String, Download> downloads = new HashMap<>();


    @Data
    public static class Download implements Serializable {
        private String path;
    }

}