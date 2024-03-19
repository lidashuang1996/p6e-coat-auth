package club.p6e.coat.auth.password;

import club.p6e.coat.common.utils.GeneratorUtil;
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
            throw new RuntimeException("Please modify the default <SEED> value. " +
                    ">> club.p6e.coat.auth.password.AuthPasswordEncryptorImpl.SEED = \"[your seed]\"");
        }
    }

    /**
     * 格式化
     *
     * @param content 密码
     * @return 格式化的内容
     */
    private String format(String content) {
        final String hex = DigestUtils.md5DigestAsHex(
                (content + SEED).getBytes(StandardCharsets.UTF_8)
        );
        final int index = ((int) hex.charAt(16)) % 24;
        return hex.substring(index) + DigestUtils.md5DigestAsHex(
                hex.substring(0, index).getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 加密
     *
     * @param random  随机
     * @param content 密码
     * @return 结果
     */
    private String execute(String random, String content) {
        final int index = ((int) random.charAt(0)) % 24;
        return random + "." + content.substring(index) + DigestUtils.md5DigestAsHex(
                (random + content.substring(0, index)).getBytes(StandardCharsets.UTF_8)
        );
    }

    /**
     * 执行密码加密
     *
     * @param content 密码
     * @return 密码加密后的内容
     */
    @Override
    public String execute(String content) {
        return execute(GeneratorUtil.random(8, true, false), format(content));
    }

    @Override
    public boolean validate(String pwd1, String pwd2) {
        if (pwd1 == null || pwd2 == null || pwd1.isEmpty() || pwd2.isEmpty()) {
            return false;
        } else {
            boolean bool = false;
            final StringBuilder random = new StringBuilder();
            for (final char ch : pwd2.toCharArray()) {
                if (ch == '.') {
                    bool = true;
                } else {
                    if (!bool) {
                        random.append(ch);
                    }
                }
            }
            if (bool) {
                return execute(random.toString(), format(pwd1)).equals(pwd2);
            } else {
                return false;
            }
        }
    }

}
