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

    public Integer getId() {
        return id;
    }

    public HashMap<String, Object> toMap() {
        return new HashMap<>();
    }

    public CloseUploadContext setMap(Map<String, Object> map) {
        return this;
    }
}
