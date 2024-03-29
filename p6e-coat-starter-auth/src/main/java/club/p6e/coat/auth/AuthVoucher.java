package club.p6e.coat.auth;

import club.p6e.coat.auth.cache.VoucherCache;
import club.p6e.coat.auth.generator.VoucherGenerator;
import club.p6e.coat.auth.model.Oauth2ClientModel;
import club.p6e.coat.auth.error.GlobalExceptionContext;
import club.p6e.coat.common.utils.SpringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AuthVoucher implements Serializable {

    public static final String PRIVATE = "PRIVATE";
    public static final String IP = "IP";

    public static final String ME = "ME";
    public static final String ME_DATE = "ME_DATE";

    public static final String INDEX = "INDEX";
    public static final String INDEX_DATE = "INDEX_DATE";

    public static final String REGISTER = "REGISTER";
    public static final String REGISTER_DATE = "REGISTER_DATE";
    public static final String ACCOUNT_PASSWORD_LOGIN = "ACCOUNT_PASSWORD_LOGIN";
    public static final String ACCOUNT_PASSWORD_CODEC_DATE = "ACCOUNT_PASSWORD_CODEC_DATE";
    public static final String ACCOUNT_PASSWORD_CODEC_MARK = "ACCOUNT_PASSWORD_CODEC_MARK";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ACCOUNT = "ACCOUNT";

    public static final String OTHER_LOGIN_TYPE = "OTHER_LOGIN_TYPE";
    public static final String OTHER_LOGIN_DATE = "OTHER_LOGIN_DATE";

    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String VERIFICATION_CODE_LOGIN_DATE = "VERIFICATION_CODE_LOGIN_DATE";
    public static final String VERIFICATION_CODE_LOGIN_MARK = "VERIFICATION_CODE_LOGIN_MARK";
    public static final String QUICK_RESPONSE_CODE_LOGIN_MARK = "QUICK_RESPONSE_CODE_LOGIN_MARK";
    public static final String QUICK_RESPONSE_CODE_LOGIN_DATE = "QUICK_RESPONSE_CODE_LOGIN_DATE";

    public static final String OAUTH2 = "OAUTH2";
    public static final String OAUTH2_DATE = "OAUTH2_DATE";
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

    private static final String VOUCHER_URL_PARAM_NAME = "voucher";

    private static final String DEVICE = "DEVICE";

    /**
     * 标记内容
     */
    private final String mark;

    /**
     * 凭证缓存对象
     */
    private final VoucherCache cache;

    /**
     * 数据对象
     */
    private final Map<String, String> data;

    /**
     * 初始化认证凭证上下文对象
     *
     * @param exchange 服务器交换对象
     * @return Mono<AuthVoucherContext> 认证凭证上下文对象
     */
    public static Mono<AuthVoucher> init(ServerWebExchange exchange) {
        System.out.println("AuthVoucher init");
        final VoucherCache cache = SpringUtil.getBean(VoucherCache.class);
        final ServerHttpRequest request = exchange.getRequest();
        final String voucher = request.getQueryParams().getFirst(VOUCHER_URL_PARAM_NAME);
        System.out.println("AuthVoucher init  " + voucher);
        if (StringUtils.hasText(voucher)) {
            return cache
                    .get(voucher)
                    .map(m -> new AuthVoucher(voucher, m, cache))
                    .switchIfEmpty(Mono.error(GlobalExceptionContext.executeVoucherException(
                            AuthVoucher.class,
                            "fun init(ServerWebExchange exchange)",
                            "Voucher request parameter does not data or expired exception."
                    )));
        } else {
            System.out.println("Voucher request parameter does not exist exception.");
            return Mono.error(GlobalExceptionContext.executeVoucherException(
                    AuthVoucher.class,
                    "fun init(ServerWebExchange exchange)",
                    "Voucher request parameter does not exist exception."
            ));
        }
    }

    public static Mono<AuthVoucher> init(String voucher) {
        final VoucherCache cache = SpringUtil.getBean(VoucherCache.class);
        return cache
                .get(voucher)
                .map(m -> new AuthVoucher(voucher, m, cache))
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeVoucherException(
                        AuthVoucher.class,
                        "fun init(ServerWebExchange exchange)",
                        "Voucher request parameter does not data or expired exception."
                )));
    }

    public static Mono<AuthVoucher> createMe() {
        return create(new HashMap<>() {{
            put(AuthVoucher.ME, "true");
            put(AuthVoucher.ME_DATE, String.valueOf(System.currentTimeMillis()));
        }});
    }

    public static Mono<AuthVoucher> createIndex() {
        return create(new HashMap<>() {{
            put(AuthVoucher.INDEX, "true");
            put(AuthVoucher.INDEX_DATE, String.valueOf(System.currentTimeMillis()));
        }});
    }

    public static Mono<AuthVoucher> createOAuth2Index(Oauth2ClientModel ocm, String type,
                                                      String redirectUri, String scope, String state) {
        return create(new HashMap<>() {{
            put(AuthVoucher.OAUTH2, "true");
            put(AuthVoucher.OAUTH2_DATE, String.valueOf(System.currentTimeMillis()));
            put(AuthVoucher.OAUTH2_SCOPE, scope);
            put(AuthVoucher.OAUTH2_RESPONSE_TYPE, type);
            put(AuthVoucher.OAUTH2_REDIRECT_URI, redirectUri);
            put(AuthVoucher.OAUTH2_CLIENT_ID, ocm.getClientId());
            put(AuthVoucher.OAUTH2_CLIENT_NAME, ocm.getClientName());
            put(AuthVoucher.OAUTH2_CLIENT_AVATAR, ocm.getClientAvatar());
            put(AuthVoucher.OAUTH2_CLIENT_DESCRIBE, ocm.getClientDescription());
            put(AuthVoucher.OAUTH2_CLIENT_RECONFIRM, String.valueOf(ocm.getReconfirm()));
            if (state != null) {
                put(AuthVoucher.OAUTH2_STATE, state);
            }
        }});
    }

    /**
     * 创建认证凭证上下文对象
     *
     * @return Mono<AuthVoucherContext> 认证凭证上下文对象
     */
    public static Mono<AuthVoucher> create(Map<String, String> data) {
        final VoucherCache cache = SpringUtil.getBean(VoucherCache.class);
        final VoucherGenerator generator = SpringUtil.getBean(VoucherGenerator.class);
        final String voucher = generator.execute();
        System.out.println("voucher  " + voucher);
        return cache
                .set(voucher, data)
                .flatMap(b -> b ? Mono.just(new AuthVoucher(voucher, data, cache)) : Mono.error(
                        GlobalExceptionContext.executeVoucherException(
                                AuthVoucher.class,
                                "fun create(Map<String, String> data)",
                                "Voucher create data cache exception."
                        )));
    }

    /**
     * 构造函数私有化
     */
    private AuthVoucher(String mark, Map<String, String> data, VoucherCache cache) {
        this.mark = mark;
        this.cache = cache;
        this.data = Collections.synchronizedMap(new HashMap<>());
        if (data != null && data.size() > 0) {
            for (final String key : data.keySet()) {
                this.data.put(key, data.get(key));
            }
        }
        System.out.println("AuthVoucher >>>> " + this);
    }

    /**
     * 获取标记
     *
     * @return 标记内容
     */
    public String getMark() {
        return mark;
    }

    public Mono<AuthVoucher> setAccount(String account) {
        final Map<String, String> map = new HashMap<>();
        map.put(ACCOUNT, account);
        return this.set(map);
    }

    public String getAccount() {
        return this.get(ACCOUNT);
    }


    public Mono<AuthVoucher> setOAuth2User(String uid, String info) {
        final Map<String, String> map = new HashMap<>();
        map.put(AuthVoucher.OAUTH2_USER_ID, uid);
        map.put(AuthVoucher.OAUTH2_USER_INFO, info);
        return this.set(map);
    }

    /**
     * 获取属性值
     *
     * @param key 属性键
     * @return 属性值
     */
    public String get(String key) {
        return data.get(key);
    }

    /**
     * 获取数据
     *
     * @return 数据对象
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * 写入数据
     *
     * @param map 数据对象
     * @return Mono<AuthVoucherContext> 认证凭证上下文对象
     */
    public Mono<AuthVoucher> set(Map<String, String> map) {
        return cache
                .set(mark, map)
                .map(b -> {
                    if (b) {
                        for (final String key : map.keySet()) {
                            data.put(key, map.get(key));
                        }
                    }
                    return this;
                });
    }


    /**
     * 写入数据
     *
     * @param map 数据对象
     * @return Mono<AuthVoucherContext> 认证凭证上下文对象
     */
    public Mono<AuthVoucher> del() {
//        return cache
//                .del(mark)
//                .map(b -> this);
        return Mono.just(this);
    }


    public boolean isOAuth2() {
        return StringUtils.hasText(this.get(AuthVoucher.OAUTH2));
    }

    public boolean isOAuth2Complete() {
        return StringUtils.hasText(this.get(AuthVoucher.OAUTH2));
    }

    public Map<String, Object> getOAuth2() {
        final String clientId = this.get(AuthVoucher.OAUTH2_CLIENT_ID);
        final String clientName = this.get(AuthVoucher.OAUTH2_CLIENT_NAME);
        final String clientAvatar = this.get(AuthVoucher.OAUTH2_CLIENT_AVATAR);
        final String clientDescribe = this.get(AuthVoucher.OAUTH2_CLIENT_DESCRIBE);
        final String clientReconfirm = this.get(AuthVoucher.OAUTH2_CLIENT_RECONFIRM);
        final Map<String, Object> client = new HashMap<>(5);
        client.put("clientId", clientId);
        client.put("clientName", clientName);
        client.put("clientAvatar", clientAvatar);
        client.put("clientDescribe", clientDescribe);
        client.put("clientReconfirm", clientReconfirm);
        return client;
    }

    public String device() {
        return "123";
    }
}
