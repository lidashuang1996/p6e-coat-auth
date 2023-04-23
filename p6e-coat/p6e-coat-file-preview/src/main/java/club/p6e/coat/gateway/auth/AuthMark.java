package club.p6e.coat.gateway.auth;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthMark {

    private static boolean STATUS = false;

    public static boolean status() {
        return STATUS;
    }

    public static void setStatus(boolean status) {
        STATUS = status;
    }
}
