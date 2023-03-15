package com.example.p6e_dawenjian_2023.context;

import org.springframework.http.codec.multipart.FilePart;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class SliceUploadContext extends HashMap<String, Object> implements Serializable {
    private FilePart filePart;
    private Integer id;
    private String signature;

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SliceUploadContext() {
    }

    public FilePart getFilePart() {
        return filePart;
    }

    public void setFilePart(FilePart filePart) {
        this.filePart = filePart;
    }

    public SliceUploadContext(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("id") != null && map.get("id") instanceof final Integer content) {
            this.setId(content);
            this.remove("id");
        }
        if (map.get("signature") != null && map.get("signature") instanceof final String content) {
            this.setSignature(content);
            this.remove("signature");
        }
        if (map.get("filePart") != null && map.get("filePart") instanceof final FilePart fp) {
            this.setFilePart(fp);
            this.remove("filePart");
        }
    }

    public Map<String, Object> toMap() {
        this.put("id", id);
        this.put("filePart", filePart);
        this.put("signature", signature);
        return this;
    }
}
