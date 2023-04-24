package club.p6e.coat.file.manage.infrastructure.error;

import com.darvi.hksi.badminton.lib.error.*;

/**
 * 全局异常上下文
 *
 * @author lidashuang
 * @version 1.0
 */
public final class GlobalExceptionContext {

    public static CustomException executeAuthException(Class<?> sc, String content) {
        return new AuthException(sc, content);
    }

    public static CustomException executeParameterException(Class<?> sc, String content) {
        return new ParameterException(sc, content);
    }

    public static CustomException executeAccountOrPasswordException(Class<?> sc, String content) {
        return new AccountOrPasswordException(sc, content);
    }

    public static CustomException executeUserNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 1, "USER_DATA_NOT_EXIST_EXCEPTION");
    }

    public static CustomException executeUserAccountExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 2, "USER_ACCOUNT_DATA_EXIST_EXCEPTION");
    }

    public static CustomException executeUserDeleteYourselfException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 3, "USER_DELETE_YOURSELF_DATA_EXCEPTION");
    }

    public static CustomException executeUserRoleNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 11, "USER_ROLE_DATA_NOT_EXIST_EXCEPTION");
    }

    public static CustomException executeUserRelationUserRoleExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 21, "USER_RELATION_USER_ROLE_DATA_EXIST_EXCEPTION");
    }

    public static CustomException executeUserRelationUserRoleNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 22, "USER_RELATION_USER_ROLE_DATA_NOT_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 31, "JURISDICTION_URL_DATA_NOT_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlGroupNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content,
                ResourceException.DEFAULT_CODE + 100 + 41, "JURISDICTION_URL_GROUP_DATA_NOT_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlGroupRelationJurisdictionUrlExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content, ResourceException.DEFAULT_CODE + 100 + 51,
                "JURISDICTION_URL_GROUP_RELATION_JURISDICTION_URL_DATA_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlGroupRelationJurisdictionUrlNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content, ResourceException.DEFAULT_CODE + 100 + 52,
                "JURISDICTION_URL_GROUP_RELATION_JURISDICTION_URL_NOT_DATA_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlGroupRelationUserRoleExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content, ResourceException.DEFAULT_CODE + 100 + 61,
                "JURISDICTION_URL_GROUP_RELATION_USER_ROLE_DATA_EXIST_EXCEPTION");
    }

    public static CustomException executeJurisdictionUrlGroupRelationUserRoleNotExistException(Class<?> sc, String content) {
        return new ResourceException(sc, content, ResourceException.DEFAULT_CODE + 100 + 62,
                "JURISDICTION_URL_GROUP_RELATION_USER_ROLE_DATA_NOT_EXIST_EXCEPTION");
    }
}
