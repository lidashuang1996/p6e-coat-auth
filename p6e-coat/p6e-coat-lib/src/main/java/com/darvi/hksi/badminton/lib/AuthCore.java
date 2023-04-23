package com.darvi.hksi.badminton.lib;

import com.darvi.hksi.badminton.lib.error.AuthException;
import com.darvi.hksi.badminton.lib.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * 认证模型对象
 *
 * @author lidashuang
 * @version 1.0
 */
@Data
@Accessors(chain = true)
public final class AuthCore implements Serializable {

    private Integer id;
    private String account;
    private Integer state;
    private Integer role;
    private String roleName;
    private String name;
    private String nickname;
    private String avatar;
    private String describe;
    private List<String> userJurisdictionList;

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    /**
     * 统一携带用户信息头
     */
    private static final String USER_HEADER = "HKSI-Badminton-User";

    private static AuthCore AC;
    private static boolean IS_DEBUG = false;

    static {
        AC = new AuthCore();
        AC.setId(1);
        AC.setState(1);
        AC.setRole(1);
        AC.setRoleName("test_role");
        AC.setName("test_name");
        AC.setNickname("test_nickname");
        AC.setAvatar("test_avatar");
        AC.setDescribe("test_describe");
        AC.setAccount("12345678910");
        AC.setUserJurisdictionList(Arrays.asList("1", "2"));
    }


    public static void setDebug(boolean status) {
        IS_DEBUG = status;
    }

    public static void setAuthCore(AuthCore ac) {
        AC = ac;
    }

    /**
     * 通过线程获取认证的实例对象
     * 通过线程获取当前线程的请求对象，从而从头获取用户的认证信息头，进而读取用户的认证数据
     *
     * @return 认证的实例对象
     */
    public static AuthCore getThreadInstance() {
        if (IS_DEBUG) {
            return AC;
        } else {
            final ServletRequestAttributes servletRequestAttributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (servletRequestAttributes != null) {
                final HttpServletRequest request = servletRequestAttributes.getRequest();
                final String content = request.getHeader(USER_HEADER);
                if (content != null) {
                    return JsonUtil.fromJson(content, AuthCore.class);
                }
            }
            throw new AuthException(AuthCore.class, "fun getThreadInstance().");
        }
    }

}
