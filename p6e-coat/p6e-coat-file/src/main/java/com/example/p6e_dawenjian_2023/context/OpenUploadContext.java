package com.example.p6e_dawenjian_2023.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class OpenUploadContext extends HashMap<String, Object> implements Serializable {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OpenUploadContext() {
    }

    public OpenUploadContext(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("name") != null && map.get("name") instanceof final String content) {
            this.setName(content);
            this.remove("name");
        }
    }

    public Map<String, Object> toMap() {
        this.put("name", name);
        return this;
    }

    public OpenUploadContext setMap(Map<String, Object> map) {
        return this;
    }
}
