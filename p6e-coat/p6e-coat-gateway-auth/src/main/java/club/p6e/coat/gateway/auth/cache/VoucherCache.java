package club.p6e.coat.gateway.auth.cache;

import club.p6e.coat.gateway.auth.cache.support.ICache;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

/**
 * 凭证会话
 *
 * @author lidashuang
 * @version 1.0
 */
public interface VoucherCache extends ICache {

    public static final String PRIVATE = "PRIVATE";
    public static final String INDEX = "INDEX";
    public static final String ACCOUNT_PASSWORD_LOGIN = "ACCOUNT_PASSWORD_LOGIN";
    public static final String ACCOUNT_PASSWORD_CODEC_MARK = "ACCOUNT_PASSWORD_CODEC_MARK";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String USER_ID = "USER_ID";
    public static final String CODE_LOGIN = "CODE_LOGIN";
    public static final String QR_CODE_VALUE = "QR_CODE_CONTENT";

    public static final String QR_CODE_LOGIN = "QR_CODE_LOGIN";

    public static final String OAUTH2 = "OAUTH2";
    public static final String OAUTH2_STATE = "OAUTH2_STATE";
    public static final String OAUTH2_SCOPE = "OAUTH2_SCOPE";
    public static final String OAUTH2_CLIENT_ID = "OAUTH2_CLIENT_ID";
    public static final String OAUTH2_REDIRECT_URI = "OAUTH2_REDIRECT_URI";
    public static final String OAUTH2_RESPONSE_TYPE = "OAUTH2_RESPONSE_TYPE";
    public static final String OAUTH2_CLIENT_RECONFIRM = "OAUTH2_CLIENT_RECONFIRM";
    public static final String OAUTH2_CLIENT_NAME = "OAUTH2_CLIENT_NAME";
    public static final String OAUTH2_CLIENT_AVATAR = "OAUTH2_CLIENT_AVATAR";
    public static final String OAUTH2_CLIENT_DESCRIBE = "OAUTH2_CLIENT_DESCRIBE";
    public static final String OAUTH2_CODE = "OAUTH2_CODE";
    public static final String OAUTH2_USER_ID = "OAUTH2_USER_ID";
    public static final String OAUTH2_USER_INFO = "OAUTH2_USER_INFO";

    /**
     * 缓存前缀
     */
    public static final String CACHE_PREFIX = "VOUCHER:";

    /**
     * 过期的时间
     */
    public static final long EXPIRATION_TIME = 60 * 30;

    /**
     * 条件注册的条件表达式
     */
    public static final String CONDITIONAL_EXPRESSION =
            "#{${p6e.auth.login.enable:false} || ${p6e.auth.oauth2.enable:false} || ${p6e.auth.register.enable:false}}";

    /**
     * 删除数据
     *
     * @param content 会话编号
     */
    public Mono<Long> del(String content);

    /**
     * 读取全部数据
     *
     * @param content 会话编号
     * @return 会话全部数据
     */
    public Mono<Map<String, String>> get(String content);

    /**
     * 绑定数据
     *
     * @param content 会话编号
     */
    public Mono<Boolean> bind(String content, Map<String, String> data);

}