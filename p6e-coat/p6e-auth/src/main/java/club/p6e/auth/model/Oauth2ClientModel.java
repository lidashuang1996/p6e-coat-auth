package club.p6e.auth.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Oauth2 Client 模型
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
@Table(Oauth2ClientModel.TABLE)
public class Oauth2ClientModel implements Serializable {
    public static final String TABLE = "p6e_oauth2_client";

    public static final String ID = "id";
    public static final String ENABLED = "enabled";
    public static final String TYPE = "type";
    public static final String SCOPE = "scope";
    public static final String REDIRECT_URI = "redirectUri";
    public static final String SECONDARY_CONFIRMATION = "secondaryConfirmation";
    public static final String CLIENT_ID = "clientId";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String CLIENT_NAME = "clientName";
    public static final String CLIENT_DESCRIBE = "clientDescribe";
    public static final String CREATE_DATE = "createDate";
    public static final String UPDATE_DATE = "updateDate";
    public static final String OPERATOR = "operator";
    public static final String VERSION = "version";
    public static final String IS_DELETE = "isDelete";

    @Id
    private Integer id;
    private Integer enabled;
    private String type;
    private String scope;
    private String redirectUri;
    private Integer reconfirm;
    private String clientId;
    private String clientSecret;
    private String clientName;
    private String clientAvatar;
    private String clientDescribe;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private String operator;
    private Integer version;
    private Integer isDelete;

    public Map<String, Object> toMap() {
        final Map<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("enabled", enabled);
        result.put("type", type);
        result.put("scope", scope);
        result.put("redirectUri", redirectUri);
        result.put("reconfirm", reconfirm);
        result.put("clientId", clientId);
        result.put("clientSecret", clientSecret);
        result.put("clientName", clientName);
        result.put("clientAvatar", clientAvatar);
        result.put("clientDescribe", clientDescribe);
        result.put("createDate", createDate);
        result.put("updateDate", updateDate);
        result.put("operator", operator);
        return result;
    }

}



