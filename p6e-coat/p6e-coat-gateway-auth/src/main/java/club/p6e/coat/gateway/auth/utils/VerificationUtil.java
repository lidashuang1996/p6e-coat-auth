package club.p6e.coat.gateway.auth.utils;

import java.util.regex.Pattern;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class VerificationUtil {

    public static boolean phone(String data) {
        return Pattern.matches("^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$", data);
    }

    public static boolean mailbox(String data) {
//        return Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\\\.[a-zA-Z0-9]+", data);
        return true;
    }
}
