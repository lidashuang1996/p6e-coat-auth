package club.p6e.cloud.auth.utils;

import java.util.List;
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

    /**
     * 验证 scope
     *
     * @param source  源
     * @param content 内容
     * @return 验证结果
     */
    public static boolean oauth2Scope(String source, String content) {
        if (source == null || content == null) {
            return false;
        } else {
            final List<String> sList = List.of(source.split(","));
            final List<String> cList = List.of(content.split(","));
            for (final String ci : cList) {
                boolean bool = false;
                for (final String si : sList) {
                    if (si.equalsIgnoreCase(ci)) {
                        bool = true;
                        break;
                    }
                }
                if (!bool) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * 验证 redirect uri
     *
     * @param source  源
     * @param content 内容
     * @return 验证结果
     */
    public static boolean oauth2RedirectUri(String source, String content) {
        if (source != null && content != null) {
            final List<String> sList = List.of(source.split(","));
            for (final String si : sList) {
                if (si.equalsIgnoreCase(content)) {
                    return true;
                }
            }
        }
        return false;
    }
}
