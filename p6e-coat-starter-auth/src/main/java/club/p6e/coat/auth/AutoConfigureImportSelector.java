package club.p6e.coat.auth;

import club.p6e.coat.auth.cache.memory.*;
import club.p6e.coat.auth.cache.redis.*;
import club.p6e.coat.auth.controller.*;
import club.p6e.coat.auth.generator.*;
import club.p6e.coat.auth.message.WebSocketMessage;
import club.p6e.coat.auth.message.WebSocketMessageImpl;
import club.p6e.coat.auth.password.AuthPasswordEncryptorImpl;
import club.p6e.coat.auth.service.*;
import club.p6e.coat.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.coat.auth.codec.PasswordTransmissionCodecImpl;
import club.p6e.coat.auth.launcher.EmailMessageLauncherImpl;
import club.p6e.coat.auth.launcher.SmsMessageLauncherImpl;
import club.p6e.coat.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.auth.repository.UserAuthRepository;
import club.p6e.coat.auth.repository.UserRepository;
import club.p6e.coat.auth.validator.AccountPasswordLoginParameterValidator;
import club.p6e.coat.common.utils.SpringUtil;
import club.p6e.coat.common.utils.TemplateParser;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 自动配置导入选择器
 *
 * @author lidashuang
 * @version 1.0
 */
public class AutoConfigureImportSelector {

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties  配置文件对象
     * @param application 应用上下文对象
     */
    public AutoConfigureImportSelector(Properties properties, ApplicationContext application) {
        // 初始化
        SpringUtil.init(application);
        this.properties = properties;

        final AutowireCapableBeanFactory factory = application.getAutowireCapableBeanFactory();
        final DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) factory;

        initOAuth2Confirm();
        /*
         * 注入需要的bean对象
         */
        if (properties.isEnable()) {
            initMePage();
            registerAuthWebFilterBean(defaultListableBeanFactory);
            registerBean(MeControllerImpl.class, defaultListableBeanFactory);
            registerBean(LogoutControllerImpl.class, defaultListableBeanFactory);
        }

        // 注册内存缓存模式依赖的对象
        if (properties.isEnable()
                && properties.getCache() != null
                && properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(ReactiveMemoryTemplate.class, defaultListableBeanFactory);
        }


        // 注册->登录对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()) {
            initLoginPage();
            registerVoucherBean(defaultListableBeanFactory);
            registerBean(AuthUserImpl.class, defaultListableBeanFactory);
            registerBean(IndexControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationLoginControllerImpl.class, defaultListableBeanFactory);
        }

        // 注册->账号密码登录对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()) {
            registerUserRepositoryBean(defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginParameterValidator.class, defaultListableBeanFactory);
        }

        // 注册->账号密码登录签名对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()
                && properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            registerAccountPasswordLoginSignatureCacheBean(defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureServiceImpl.class, defaultListableBeanFactory);
            registerBean(PasswordTransmissionCodecImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureControllerImpl.class, defaultListableBeanFactory);
        }

        // 注册->验证码登录对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable()) {
            registerLauncherBean(defaultListableBeanFactory);
            registerUserRepositoryBean(defaultListableBeanFactory);
            registerVerificationCodeLoginCacheBean(defaultListableBeanFactory);
            registerBean(VerificationCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeLoginGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainControllerImpl.class, defaultListableBeanFactory);
        }

        // 注册->二维码登录对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable()) {
            registerUserRepositoryBean(defaultListableBeanFactory);
            registerQrCodeLoginCacheBean(defaultListableBeanFactory);
            registerBean(QrCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeLoginGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainControllerImpl.class, defaultListableBeanFactory);

            if (properties.getLogin().getQrCode().getWebSocket().isEnable()) {
                registerQrCodeWebSocketBean(defaultListableBeanFactory);
            }
        }

        // 注册->其它登录对象
        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && !properties.getLogin().getOthers().isEmpty()) {
            initOtherLoginConfig();
            registerStateOtherLoginCacheBean(defaultListableBeanFactory);
            registerBean(QqOtherLoginController.class, defaultListableBeanFactory);
//            registerBean(QqOtherLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(StateOtherLoginGenerator.class, defaultListableBeanFactory);
        }

        // 注册->OAuth2对象
        if (properties.isEnable()
                && properties.getOauth2().isEnable()) {
            initLoginPage();
            registerOauth2RepositoryBean(defaultListableBeanFactory);
            registerBean(AuthUserImpl.class, defaultListableBeanFactory);
            registerBean(AuthOAuth2ClientImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2AuthorizeServiceImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2TokenServiceImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2ConfirmServiceImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2AuthorizeControllerImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2TokenControllerImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2ReconfirmControllerImpl.class, defaultListableBeanFactory);
        }

        // 注册->OAuth2客户端模式对象
        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getClient().isEnable()) {
            registerOauth2TokenClientAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
        }

        // 注册->OAuth2密码模式对象
        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getPassword().isEnable()) {
            registerOauth2TokenUserAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2UserOpenIdGeneratorImpl.class, defaultListableBeanFactory);
        }

        // 注册->OAuth2授权模式对象
        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getAuthorizationCode().isEnable()) {
            registerOauth2CodeCacheBean(defaultListableBeanFactory);
            registerOauth2TokenUserAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2CodeGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(OAuth2UserOpenIdGeneratorImpl.class, defaultListableBeanFactory);
        }

        // 注册->注册对象
        if (properties.isEnable()
                && properties.getRegister().isEnable()) {
            initRegisterPage();
            registerUserRepositoryBean(defaultListableBeanFactory);
            registerRegisterCodeCacheBean(defaultListableBeanFactory);
            registerBean(RegisterServiceImpl.class, defaultListableBeanFactory);
            registerBean(RegisterObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(RegisterControllerImpl.class, defaultListableBeanFactory);
            registerBean(RegisterObtainControllerImpl.class, defaultListableBeanFactory);
            registerBean(RegisterCodeGeneratorImpl.class, defaultListableBeanFactory);

            // 注册->第三方登录需要未注册需要进行注册的对象
            if (properties.getRegister().isEnableOtherLoginBinding()) {
                registerRegisterOtherLoginCacheBean(defaultListableBeanFactory);
                registerBean(RegisterOtherLoginGeneratorImpl.class, defaultListableBeanFactory);
            }
        }

        if (properties.isEnable()
                && properties.getForgotPassword().isEnable()) {
            initForgotPassword();
            registerForgotPasswordCodeCacheBean(defaultListableBeanFactory);
            registerBean(ForgotPasswordServiceImpl.class, defaultListableBeanFactory);
            registerBean(ForgotPasswordObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(ForgotPasswordCodeGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(ForgotPasswordControllerImpl.class, defaultListableBeanFactory);
            registerBean(ForgotPasswordCodeObtainControllerImpl.class, defaultListableBeanFactory);
        }
    }

    /**
     * 读取文件内容
     *
     * @return 文件的内容
     */
    @SuppressWarnings("ALL")
    private String file(String path) {
        if (path != null) {
            if (path.startsWith("classpath:")) {
                InputStream inputStream = null;
                InputStreamReader inputStreamReader = null;
                BufferedReader bufferedReader = null;
                final StringBuilder sb = new StringBuilder();
                try {
                    inputStream = this.getClass().getClassLoader().getResourceAsStream(
                            path.substring("classpath:".length())
                    );
                    if (inputStream != null) {
                        inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                        bufferedReader = new BufferedReader(inputStreamReader);
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                    }
                    return sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStreamReader != null) {
                        try {
                            inputStreamReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                FileReader fileReader = null;
                final StringBuilder sb = new StringBuilder();
                if (path.startsWith("file:")) {
                    path = path.substring("file:".length());
                }
                try {
                    fileReader = new FileReader(path);
                    int ch;
                    while ((ch = fileReader.read()) != -1) {
                        sb.append((char) ch);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return sb.toString();
            }
        }
        return "";
    }

    /**
     * 初始化我的页面内容
     */
    private void initMePage() {
        AuthPage.setMe(MediaType.TEXT_HTML, file(properties.getPage().getMe()));
    }

    /**
     * 初始化登录页面内容
     */
    private void initLoginPage() {
        AuthPage.setLogin(MediaType.TEXT_HTML, file(properties.getPage().getLogin()));
    }

    /**
     * 初始化注册页面内容
     */
    private void initRegisterPage() {
        AuthPage.setRegister(MediaType.TEXT_HTML, file(properties.getPage().getRegister()));
    }

    /**
     * 初始化忘记密码页面内容
     */
    private void initForgotPassword() {
        AuthPage.setForgotPassword(MediaType.TEXT_HTML, file(properties.getPage().getForgotPassword()));
    }

    /**
     * 初始化忘记密码页面内容
     */
    private void initOAuth2Confirm() {
        AuthPage.setOAuth2Confirm(MediaType.TEXT_HTML, file(properties.getPage().getLogin()));
    }

    /**
     * 初始化其它登录的配置文件
     */
    private void initOtherLoginConfig() {
        final Map<String, Properties.Login.Other> others = properties.getLogin().getOthers();
        for (final String key : others.keySet()) {
            final Properties.Login.Other other = others.get(key);
            final Map<String, String> data = new HashMap<>();
            final Map<String, String> varData = new HashMap<>();
            final Map<String, String> templateData = new HashMap<>();
            final Map<String, String> config = other.getConfig();
            for (final String ck : config.keySet()) {
                if (ck.startsWith("#")) {
                    varData.put(ck, config.get(ck));
                }
                if (ck.startsWith("@")) {
                    templateData.put(ck, config.get(ck));
                }
                if (!ck.startsWith("#") || !ck.startsWith("@")) {
                    data.put(ck, config.get(ck));
                }
            }
            varData.replaceAll((k, v) -> TemplateParser.execute(v, data));
            templateData.replaceAll((k, v) -> TemplateParser.execute(v, varData));
            config.putAll(varData);
            config.putAll(templateData);
        }
    }

    /**
     * 注册认证过滤器
     *
     * @param factory 上下文对象工厂
     */
    private void registerAuthWebFilterBean(DefaultListableBeanFactory factory) {
        final Properties.Bean validator = properties.getAuth().getValidator();
        final Properties.Bean authority = properties.getAuth().getAuthority();
        final String[] dependency1 = new String[]{
                "club.p6e.coat.auth.AuthJsonWebTokenCipher"
        };
        final String[] dependency2 = switch (properties.getCache().getType()) {
            case REDIS -> new String[]{
                    "club.p6e.coat.auth.cache.redis.AuthRedisCache",
            };
            case MEMORY -> new String[]{
                    "club.p6e.coat.auth.cache.memory.support.ReactiveMemoryTemplate",
                    "club.p6e.coat.auth.cache.memory.AuthMemoryCache",
            };
        };
        switch (validator.getName().toUpperCase()) {
            case Properties.Auth.HTTP_COOKIE_CACHE -> {
                validator.setName("club.p6e.coat.auth.certificate.HttpCookieCacheCertificateValidator");
                validator.setDependency(dependency2);
            }
            case Properties.Auth.HTTP_LOCAL_CACHE -> {
                validator.setName("club.p6e.coat.auth.certificate.HttpLocalStorageCacheCertificateValidator");
                validator.setDependency(dependency2);
            }
            case Properties.Auth.HTTP_COOKIE_JWT -> {
                validator.setName("club.p6e.coat.auth.certificate.HttpCookieJsonWebTokenCertificateValidator");
                validator.setDependency(dependency1);
            }
            case Properties.Auth.HTTP_LOCAL_JWT -> {
                validator.setName("club.p6e.coat.auth.certificate.HttpLocalStorageJsonWebTokenCertificateValidator");
                validator.setDependency(dependency1);
            }
        }
        switch (authority.getName().toUpperCase()) {
            case Properties.Auth.HTTP_COOKIE_CACHE -> {
                authority.setName("club.p6e.coat.auth.certificate.HttpCookieCacheCertificateAuthority");
                authority.setDependency(dependency2);
            }
            case Properties.Auth.HTTP_LOCAL_CACHE -> {
                authority.setName("club.p6e.coat.auth.certificate.HttpLocalStorageCacheCertificateAuthority");
                authority.setDependency(dependency2);
            }
            case Properties.Auth.HTTP_COOKIE_JWT -> {
                authority.setName("club.p6e.coat.auth.certificate.HttpCookieJsonWebTokenCertificateAuthority");
                authority.setDependency(dependency1);
            }
            case Properties.Auth.HTTP_LOCAL_JWT -> {
                authority.setName("club.p6e.coat.auth.certificate.HttpLocalStorageJsonWebTokenCertificateAuthority");
                authority.setDependency(dependency1);
            }
        }
        registerPropertiesBean(validator, factory);
        registerPropertiesBean(authority, factory);
        registerBean(AuthPathMatcher.class, factory);
        registerBean(AuthWebFilter.class, factory, true, false);
        final AuthPathMatcher matcher = factory.getBean(AuthPathMatcher.class);
        for (final String path : properties.getInterceptor()) {
            matcher.register(path);
        }
    }

    /**
     * 注册凭证过滤器
     *
     * @param factory 上下文对象工厂
     */
    private void registerVoucherBean(DefaultListableBeanFactory factory) {
        registerVoucherCacheBean(factory);
        registerBean(AuthVoucher.class, factory);
        registerBean(VoucherGeneratorImpl.class, factory);
    }

    /**
     * 注册发射器
     *
     * @param factory 上下文对象工厂
     */
    private void registerLauncherBean(DefaultListableBeanFactory factory) {
        registerBean(SmsMessageLauncherImpl.class, factory);
        registerBean(EmailMessageLauncherImpl.class, factory);
    }

    /**
     * 注册二维码登录缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerQrCodeWebSocketBean(DefaultListableBeanFactory factory) {
        registerWebSocketCacheBean(factory);
        registerBean(WebSocketMessageImpl.class, factory);
        factory.getBean(WebSocketMessage.class).startup();
    }

    /**
     * 注册用户的存储库
     *
     * @param factory 上下文对象工厂
     */
    private void registerUserRepositoryBean(DefaultListableBeanFactory factory) {
        registerBean(UserRepository.class, factory);
        registerBean(UserAuthRepository.class, factory);
    }

    /**
     * 注册 OAuth2 的存储库
     *
     * @param factory 上下文对象工厂
     */
    private void registerOauth2RepositoryBean(DefaultListableBeanFactory factory) {
        registerUserRepositoryBean(factory);
        registerBean(Oauth2ClientRepository.class, factory);
    }

    /**
     * 注册凭证缓存过滤器
     *
     * @param factory 上下文对象工厂
     */
    private void registerVoucherCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(VoucherRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(VoucherMemoryCache.class, factory);
        }
    }

    /**
     * 注册登录签名缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerAccountPasswordLoginSignatureCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(PasswordSignatureRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(PasswordSignatureMemoryCache.class, factory);
        }
    }

    /**
     * 注册验证码登录缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerVerificationCodeLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(VerificationCodeLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(VerificationCodeLoginMemoryCache.class, factory);
        }
    }

    /**
     * 注册二维码登录缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerQrCodeLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(QrCodeLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(QrCodeLoginMemoryCache.class, factory);
        }
    }

    /**
     * 注册二维码登录 WebSocket 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerWebSocketCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(QrCodeWebSocketAuthRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(QrCodeWebSocketAuthMemoryCache.class, factory);
        }
    }

    /**
     * 注册 OAuth2 令牌客户端认证缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerOauth2TokenClientAuthCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2TokenClientAuthRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2TokenClientAuthMemoryCache.class, factory);
        }
    }

    /**
     * 注册 OAuth2 令牌用户认证缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerOauth2TokenUserAuthCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2TokenUserAuthRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2TokenUserAuthMemoryCache.class, factory);
        }
    }

    /**
     * 注册 OAuth2 code 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerOauth2CodeCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2CodeRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2CodeMemoryCache.class, factory);
        }
    }

    /**
     * 其它登录 state 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerStateOtherLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(StateOtherLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(StateOtherLoginMemoryCache.class, factory);
        }
    }

    /**
     * 其它登录 state 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerRegisterOtherLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(RegisterOtherLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(RegisterOtherLoginMemoryCache.class, factory);
        }
    }

    /**
     * 注册 Register code 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerRegisterCodeCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(RegisterCodeRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(RegisterCodeMemoryCache.class, factory);
        }
    }

    /**
     * 注册 ForgotPassword code 缓存
     *
     * @param factory 上下文对象工厂
     */
    private void registerForgotPasswordCodeCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(ForgotPasswordCodeRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(ForgotPasswordCodeMemoryCache.class, factory);
        }
    }

    /**
     * 注册 bean 服务
     *
     * @param bc      bean 的类型
     * @param factory 上下文对象工厂
     */
    private synchronized void registerBean(Class<?> bc, DefaultListableBeanFactory factory) {
        registerBean(bc, factory, true, true);
    }

    /**
     * 注册 bean 服务
     *
     * @param bc               bean 的类型
     * @param factory          上下文对象工厂
     * @param isScanSelf       注册之前是否扫描自身类型，如果存在就不进行注册
     * @param isScanInterfaces 注册之前是否扫描自身接口类型，如果存在就不进行注册
     */
    private synchronized void registerBean(
            Class<?> bc,
            DefaultListableBeanFactory factory,
            boolean isScanSelf,
            boolean isScanInterfaces
    ) {
        if (isScanSelf && isExistBean(bc, factory)) {
            return;
        }
        if (isScanInterfaces) {
            final Class<?>[] interfaces = bc.getInterfaces();
            for (final Class<?> item : interfaces) {
                if (isExistBean(item, factory)) {
                    return;
                }
            }
        }
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(bc);
        factory.registerBeanDefinition(bc.getName(), beanDefinition);
    }

    /**
     * 是否存在 bean 类型对象
     *
     * @param bc      bean 的类型
     * @param factory 上下文对象工厂
     * @return 是否存在 bean 对象
     */
    private boolean isExistBean(Class<?> bc, DefaultListableBeanFactory factory) {
        try {
            if (!factory.containsBean(bc.getName())) {
                factory.getBean(bc);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 注册属性 bean 服务
     *
     * @param bean    配置文件的 bean 对象
     * @param factory 上下文对象工厂
     */
    private synchronized void registerPropertiesBean(Properties.Bean bean, DefaultListableBeanFactory factory) {
        try {
            for (final String item : bean.getDependency()) {
                registerBean(Class.forName(item), factory);
            }
            registerBean(Class.forName(bean.getName()), factory);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
