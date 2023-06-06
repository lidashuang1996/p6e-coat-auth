package club.p6e.cloud.test.error;


/**
 * 全局异常上下文
 *
 * @author lidashuang
 * @version 1.0
 */
public final class GlobalExceptionContext {

    public static Oauth2ParameterException executeOauth2ParameterException(Class<?> sc, String error, String content) {
        return new Oauth2ParameterException(sc, error, content);
    }
    public static Oauth2ScopeException executeOauth2ScopeException(Class<?> sc, String error, String content) {
        return new Oauth2ScopeException(sc, error, content);
    }
    public static Oauth2ClientException executeOauth2ClientException(Class<?> sc, String error, String content) {
        return new Oauth2ClientException(sc, error, content);
    }
    public static Oauth2RedirectUriException executeOauth2RedirectUriException(Class<?> sc, String error, String content) {
        return new Oauth2RedirectUriException(sc, error, content);
    }
    public static ParameterException executeParameterException(Class<?> sc, String error, String content) {
        return new ParameterException(sc, error, content);
    }

    public static ParameterException executeServiceNotSupportException(Class<?> sc, String error, String content) {
        return new ParameterException(sc, error, content);
    }


    public static ServiceNotEnabledException executeServiceNotEnabledException(Class<?> sc, String error, String content) {
        return new ServiceNotEnabledException(sc, error, content);
    }

    public static ServiceNotEnabledException executeTypeNotSupportedException(Class<?> sc, String error, String content) {
        return new ServiceNotEnabledException(sc, error, content);
    }

    public static CacheException executeCacheException(Class<?> sc, String error, String content) {
        return new CacheException(sc, error, content);
    }

    public static QrCodeDataNullException executeQrCodeDataNullException(Class<?> sc, String error, String content) {
        return new QrCodeDataNullException(sc, error, content);
    }

    public static CacheException executeUserNotExistException(Class<?> sc, String error, String content) {
        return new CacheException(sc, error, content);
    }

    public static VoucherException executeVoucherException(Class<?> sc, String error, String content) {
        return new VoucherException(sc, error, content);
    }


    public static AccountPasswordLoginTransmissionException exceptionAccountPasswordLoginTransmissionException(Class<?> sc, String error, String content) {
        return new AccountPasswordLoginTransmissionException(sc, error, content);
    }


    public static AccountPasswordLoginAccountOrPasswordException exceptionAccountPasswordLoginAccountOrPasswordException(Class<?> sc, String error, String content) {
        return new AccountPasswordLoginAccountOrPasswordException(sc, error, content);
    }


    public static DataFormatException exceptionDataFormatException(Class<?> sc, String error, String content) {
        return new DataFormatException(sc, error, content);
    }

    public static AccountException exceptionAccountException(Class<?> sc, String error, String content) {
        return new AccountException(sc, error, content);
    }

    public static AuthException exceptionAuthException(Class<?> sc, String error, String content) {
        return new AuthException(sc, error, content);
    }

    public static VerificationCodeException exceptionVerificationCodeException(Class<?> sc, String error, String content) {
        return new VerificationCodeException(sc, error, content);
    }

    public static QuickResponseCodeException exceptionQuickResponseCodeException(Class<?> sc, String error, String content) {
        return new QuickResponseCodeException(sc, error, content);
    }

}
