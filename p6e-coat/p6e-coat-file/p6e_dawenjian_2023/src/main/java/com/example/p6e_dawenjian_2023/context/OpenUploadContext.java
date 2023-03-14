package com.example.p6e_dawenjian_2023.context;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
@Data
public class OpenUploadContext extends HashMap<String, Object> implements Serializable {

    private String name;

    public OpenUploadContext() {
    }

    public OpenUploadContext(Map<String, Object> map) {
        this.name = name;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>();
    }

    public OpenUploadContext setMap(Map<String, Object> map) {
        return this;
    }
}
