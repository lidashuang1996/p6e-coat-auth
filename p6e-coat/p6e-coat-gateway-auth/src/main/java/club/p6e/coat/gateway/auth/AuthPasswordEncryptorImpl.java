package club.p6e.coat.gateway.auth;

import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
@Component
public class AuthPasswordEncryptorImpl implements AuthPasswordEncryptor {

    @Override
    public String execute(String content) {
        return content + "123456";
    }

}
