package com.example.p6e_dawenjian_2023.context;

import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class SimpleUploadContext extends HashMap<String, Object> implements Serializable {

    private FilePart filePart;

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    public SimpleUploadContext() {

    }

    public SimpleUploadContext(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("filePart") != null && map.get("filePart") instanceof final FilePart fp) {
            this.setFilePart(fp);
            this.remove("filePart");
        }
    }

    public Map<String, Object> toMap() {
        this.put("filePart", filePart);
        return this;
    }
}
