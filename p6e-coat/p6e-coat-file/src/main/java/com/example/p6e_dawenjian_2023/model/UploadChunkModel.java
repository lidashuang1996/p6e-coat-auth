package com.example.p6e_dawenjian_2023.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件块上传模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Table(UploadChunkModel.TABLE)
@Accessors(chain = true)
public class UploadChunkModel implements Serializable {

    public static final String TABLE = "hksi_file_upload_chunk";

    public static final String ID = "id";
    public static final String FID = "fid";
    public static final String NAME = "name";
    public static final String DATE = "date";
    public static final String ACTION = "action";
    public static final String OPERATOR = "operator";

    @Id
    private Integer id;
    private Integer fid;
    private String name;
    private String action;
    private LocalDateTime date;
    private String operator;
    public Map<String, Object> toMap() {
        final Map<String, Object> map = new HashMap<>(8);
        map.put("id", id);
        map.put("fid", fid);
        map.put("name", name);
        map.put("action", action);
        map.put("date", date);
        map.put("operator", operator);
        return map;
    }
}
