package com.example.p6e_dawenjian_2023.context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class CloseUploadContext extends HashMap<String, Object> implements Serializable {

    private Integer id;

    public CloseUploadContext() {
    }

    public CloseUploadContext(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("id") != null && map.get("id") instanceof final Integer content) {
            this.setId(content);
            this.remove("id");
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Map<String, Object> toMap() {
        this.put("id", id);
        return this;
    }

    public CloseUploadContext setMap(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("id") != null && map.get("id") instanceof final Integer content) {
            this.setId(content);
            this.remove("id");
        }
        return this;
    }
}
