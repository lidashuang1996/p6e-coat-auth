package club.p6e.coat.gateway.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthForeignMinistryVisaTemplate {

    private Map<String, Object> attribute = new HashMap<>();
    private JsonSerializeDeserializeAuthentication authentication;

    public static AuthForeignMinistryVisaTemplate create(JsonSerializeDeserializeAuthentication authentication) {
        return new AuthForeignMinistryVisaTemplate(authentication);
    }

    public AuthForeignMinistryVisaTemplate(JsonSerializeDeserializeAuthentication authentication) {
        this.authentication = authentication;
    }


    public JsonSerializeDeserializeAuthentication getAuthentication() {
        return authentication;
    }


    public static AuthForeignMinistryVisaTemplate deserialization(String content) {

    }

    public String serialize () {

    }

    public String getId() {
        return authentication.getId();
    }

    public void setAttribute(String key, String value) {
        attribute.put(key, value);
    }

}
