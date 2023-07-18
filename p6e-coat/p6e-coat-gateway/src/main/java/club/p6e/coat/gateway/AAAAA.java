package club.p6e.coat.gateway;

import club.p6e.coat.gateway.auth.AuthPasswordEncryptor;
import org.springframework.stereotype.Component;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AAAAA implements AuthPasswordEncryptor {

    @Override
    public String execute(String content) {
        System.out.println("xxxxxxx  " + content);
        return "content" + content;
    }

}
