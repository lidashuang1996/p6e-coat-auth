package com.example.p6e_dawenjian_2023.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class DownloadContext extends HashMap<String, Object> implements Serializable {
    private String node;
    private String path;

    public DownloadContext() {

    }

    public DownloadContext(Map<String, Object> map) {

    }

    public String getNode() {
        return node;
    }

    public String getPath() {
        return path;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>();
    }

    public DownloadContext setMap(Map<String, Object> map) {
        return this;
    }
}
