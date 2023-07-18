package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.Properties;
import club.p6e.coat.gateway.auth.context.Oauth2Context;
import club.p6e.coat.gateway.auth.error.GlobalExceptionContext;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * OAUTH2 认证服务的默认实现
 *
 * @author lidashuang
 * @version 1.0
 */
public class Oauth2AuthServiceDefaultImpl implements Oauth2AuthService {

    /**
     * CODE 类型
     */
    private static final String CODE_TYPE = "CODE";

    /**
     * 主页的服务对象
     */
    private final IndexService service;

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * OAUTH CLIENT2 存储库
     */
    private final Oauth2ClientRepository repository;

    /**
     * 构造方法初始化
     *
     * @param service    主页服务对象
     * @param properties 配置文件对象
     * @param repository OAUTH CLIENT2 存储库
     */
    public Oauth2AuthServiceDefaultImpl(
            IndexService service,
            Properties properties,
            Oauth2ClientRepository repository) {
        this.service = service;
        this.properties = properties;
        this.repository = repository;

    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, Oauth2Context.Auth.Request param) {
        final String responseType = param.getResponseType();
        if (CODE_TYPE.equalsIgnoreCase(responseType)) {
            // 判断是否启用 CODE 模型
            if (properties.getOauth2().getAuthorizationCode().isEnable()) {
                // 如果启用 CODE 模型就执行 CODE 类型的代码
                return executeCodeType(exchange, param);
            } else {
                return Mono.error(GlobalExceptionContext.executeTypeNotSupportedException(
                        this.getClass(),
                        "fun execute(ServerWebExchange exchange, Oauth2Context.Auth.Request param)",
                        "Oauth2 auth type [" + responseType + "] not supported exception."
                ));
            }
        }
        return Mono.error(GlobalExceptionContext.executeTypeNotSupportedException(
                this.getClass(),
                "fun execute(Oauth2Context.Auth.Request param).",
                "Oauth2 auth service not enabled exception."
        ));
    }

    /**
     * 验证 scope
     *
     * @param source  源
     * @param content 内容
     * @return 验证结果
     */
    private boolean checkScope(String source, String content) {
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
    private boolean checkRedirectUri(String source, String content) {
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

    /**
     * 执行 CODE 类型的认证处理
     *
     * @param param 请求对象
     * @return 结果对象
     */
    private Mono<Void> executeCodeType(ServerWebExchange exchange, Oauth2Context.Auth.Request param) {
        final String scope = param.getScope();
        final String clientId = param.getClientId();
        final String redirectUri = param.getRedirectUri();
        return repository
                .findByClientId(clientId)
                .switchIfEmpty(Mono.error(GlobalExceptionContext.executeOauth2ClientException(
                        this.getClass(),
                        "fun executeCodeType(ServerWebExchange exchange, Oauth2Context.Auth.Request param)",
                        "Oauth2 code type mode client id exception."
                )))
                .flatMap(m -> {
                    // 验证作用域
                    if (!checkScope(m.getScope(), scope)) {
                        return Mono.error(GlobalExceptionContext.executeOauth2ScopeException(
                                this.getClass(),
                                "fun executeCodeType(ServerWebExchange exchange, Oauth2Context.Auth.Request param)",
                                "Oauth2 code type mode check scope exception."
                        ));
                    }
                    // 验证重定向
                    if (!checkRedirectUri(m.getRedirectUri(), redirectUri)) {
                        return Mono.error(GlobalExceptionContext.executeOauth2RedirectUriException(
                                this.getClass(),
                                "fun executeCodeType(ServerWebExchange exchange, Oauth2Context.Auth.Request param)",
                                "Oauth2 code type mode check redirect uri exception."
                        ));
                    }
                    final String state = param.getState();
                    final Map<String, String> map = new HashMap<>(5);
                    if (state != null) {
                        map.put(AuthVoucher.OAUTH2_STATE, state);
                    }
                    map.put(AuthVoucher.OAUTH2, "true");
                    map.put(AuthVoucher.OAUTH2_DATE, String.valueOf(System.currentTimeMillis()));
                    map.put(AuthVoucher.OAUTH2_SCOPE, scope);
                    map.put(AuthVoucher.OAUTH2_CLIENT_ID, clientId);
                    map.put(AuthVoucher.OAUTH2_REDIRECT_URI, redirectUri);
                    map.put(AuthVoucher.OAUTH2_CLIENT_NAME, m.getClientName());
                    map.put(AuthVoucher.OAUTH2_CLIENT_AVATAR, m.getClientAvatar());
                    map.put(AuthVoucher.OAUTH2_CLIENT_DESCRIBE, m.getClientDescribe());
                    map.put(AuthVoucher.OAUTH2_CLIENT_RECONFIRM, String.valueOf(m.getReconfirm()));
                    map.put(AuthVoucher.OAUTH2_RESPONSE_TYPE, CODE_TYPE);
                    return Mono.just(map);
                })
                .flatMap(m -> service.execute(exchange, m));

    }
}
