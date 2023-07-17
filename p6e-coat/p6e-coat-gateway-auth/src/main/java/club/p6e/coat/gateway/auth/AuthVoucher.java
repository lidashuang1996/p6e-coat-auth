package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.cache.VoucherCache;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.generator.VoucherGenerator;
import club.p6e.coat.gateway.auth.utils.SpringUtil;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
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
    public static final String INDEX = "INDEX";
    public static final String INDEX_DATE = "INDEX_DATE";
    public static final String ACCOUNT_PASSWORD_LOGIN = "ACCOUNT_PASSWORD_LOGIN";
    public static final String ACCOUNT_PASSWORD_CODEC_DATE = "ACCOUNT_PASSWORD_CODEC_DATE";
    public static final String ACCOUNT_PASSWORD_CODEC_MARK = "ACCOUNT_PASSWORD_CODEC_MARK";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String ACCOUNT = "ACCOUNT";

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
        return cache
                .del(mark)
                .map(b -> this);
    }


    public Map<String, Object> oauth2() {
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
