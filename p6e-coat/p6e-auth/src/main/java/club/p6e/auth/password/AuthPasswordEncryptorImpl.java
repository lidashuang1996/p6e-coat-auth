package club.p6e.auth.password;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 认证密码加密
 *
 * @author lidashuang
 * @version 1.0
 */
public class AuthPasswordEncryptorImpl implements AuthPasswordEncryptor {

    /**
     * 种子
     */
    public static final String SEED = "yu#u2wu3wf737ztxc@xntut34hzw#tn2";
    private static final String DEFAULT_SEED = "yu#u2wu3wf737ztxc@xntut34hzw#tns";

    /**
     * 构造方法初始化
     */
    @SuppressWarnings("ALL")
    public AuthPasswordEncryptorImpl() {
        if (DEFAULT_SEED.equals(SEED)) {
            throw new RuntimeException("Please modify the default <SEE> value. " +
                    ">> club.p6e.auth.password.AuthPasswordEncryptorImpl.SEED = \"[your seed]\"");
        }
    }

    /**
     * 执行密码加密
     *
     * @param content 密码
     * @return 密码加密后的内容
     */
    @Override
    public String execute(String content) {
        final String hex = DigestUtils.md5DigestAsHex(
                (content + SEED).getBytes(StandardCharsets.UTF_8)
        );
        final int index = ((int) hex.charAt(16)) % 24;
        return hex.substring(index) + DigestUtils.md5DigestAsHex(
                hex.substring(0, index).getBytes(StandardCharsets.UTF_8)
        );
    }

}
