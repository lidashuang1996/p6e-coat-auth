package club.p6e.cloud.auth;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthPasswordEncryptorImpl implements AuthPasswordEncryptor {

    @Override
    public String execute(String content) {
        return content + "123456";
    }

}
