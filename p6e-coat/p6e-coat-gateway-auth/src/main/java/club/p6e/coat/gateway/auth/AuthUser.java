package club.p6e.coat.gateway.auth;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface AuthUser {

    public static Class<? extends AuthUser> SUPPORT = AuthUserDetails.class;

    public String id();

    public String password();

    public Map<String, Object> toMap();

}
