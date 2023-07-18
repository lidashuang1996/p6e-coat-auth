package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.cache.memory.*;
import club.p6e.coat.gateway.auth.cache.memory.support.ReactiveMemoryTemplate;
import club.p6e.coat.gateway.auth.cache.redis.*;
import club.p6e.coat.gateway.auth.codec.AccountPasswordLoginTransmissionCodecImpl;
import club.p6e.coat.gateway.auth.controller.*;
import club.p6e.coat.gateway.auth.generator.*;
import club.p6e.coat.gateway.auth.generator.AuthTokenGeneratorImpl;
import club.p6e.coat.gateway.auth.launcher.EmailMessageLauncherImpl;
import club.p6e.coat.gateway.auth.launcher.SmsMessageLauncherImpl;
import club.p6e.coat.gateway.auth.repository.Oauth2ClientRepository;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.service.*;
import club.p6e.coat.gateway.auth.validator.AccountPasswordLoginParameterValidator;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;

/**
 * 自动配置导入选择器
 *
 * @author lidashuang
 * @version 1.0
 */
public class AutoConfigureImportSelector2 {

    private final Properties properties;

    public AutoConfigureImportSelector2(
            Properties properties,
            ApplicationContext applicationContext
    ) {
        this.properties = properties;
        System.out.println("AutoConfigureImportSelector2");
        final AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        final DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) factory;

        if (properties.isEnable()) {
            registerAuthWebFilterBean(defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getCache() != null
                && properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(ReactiveMemoryTemplate.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getLogin().isEnable()) {
            registerVoucherBean(defaultListableBeanFactory);
            registerBean(IndexServiceImpl.class, defaultListableBeanFactory);
            registerBean(IndexControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationLoginControllerImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()) {
            registerRepositoryBean(defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginParameterValidator.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()
                && properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            registerAccountPasswordLoginSignatureCacheBean(defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginTransmissionCodecImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureServiceImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginSignatureControllerImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable()) {
            registerLauncherBean(defaultListableBeanFactory);
            registerRepositoryBean(defaultListableBeanFactory);
            registerVerificationCodeLoginCacheBean(defaultListableBeanFactory);
            registerBean(CodeLoginGeneratorDefaultImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainControllerImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable()) {
            registerRepositoryBean(defaultListableBeanFactory);
            registerQrCodeLoginCacheBean(defaultListableBeanFactory);
            registerBean(QrCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainControllerImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeLoginGeneratorDefaultImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getOauth2().isEnable()) {
            registerOauth2RepositoryBean(defaultListableBeanFactory);
            registerBean(IndexServiceImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2AuthServiceImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2AuthControllerImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2ConfirmServiceImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2ConfirmControllerImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2TokenServiceImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2TokenControllerImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getClient().isEnable()) {
            registerOauth2TokenClientAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getPassword().isEnable()) {
            registerOauth2TokenUserAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2UserOpenIdGeneratorDefaultImpl.class, defaultListableBeanFactory);
        }

        if (properties.isEnable()
                && properties.getOauth2().isEnable()
                && properties.getOauth2().getAuthorizationCode().isEnable()) {
            registerOauth2CodeCache(defaultListableBeanFactory);
            registerOauth2TokenUserAuthCacheBean(defaultListableBeanFactory);
            registerBean(AuthTokenGeneratorImpl.class, defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(Oauth2UserOpenIdGeneratorDefaultImpl.class, defaultListableBeanFactory);
        }

        // -------------------
        registerBean(AuthExceptionHandlerWebFilter.class, defaultListableBeanFactory);
    }

    private void registerVoucherBean(DefaultListableBeanFactory factory) {
        registerVoucherCacheBean(factory);
        registerBean(AuthVoucher.class, factory);
        registerBean(VoucherGeneratorDefaultImpl.class, factory);
    }

    private void registerRepositoryBean(DefaultListableBeanFactory factory) {
        registerBean(UserRepository.class, factory);
        registerBean(AuthUserDetails.class, factory);
        registerBean(UserAuthRepository.class, factory);
    }

    private void registerOauth2RepositoryBean(DefaultListableBeanFactory factory) {
        registerRepositoryBean(factory);
        registerBean(Oauth2ClientRepository.class, factory);
    }

    private void registerOauth2CodeCache(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2CodeRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2CodeMemoryCache.class, factory);
        }
    }

    private void registerVerificationCodeLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(VerificationCodeLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(VerificationCodeLoginMemoryCache.class, factory);
        }
    }

    private void registerOauth2TokenClientAuthCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2TokenClientAuthRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2TokenClientAuthMemoryCache.class, factory);
        }
    }

    private void registerOauth2TokenUserAuthCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(Oauth2TokenUserAuthRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(Oauth2TokenUserAuthMemoryCache.class, factory);
        }
    }

    private void registerAccountPasswordLoginSignatureCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(AccountPasswordLoginSignatureRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(AccountPasswordLoginSignatureMemoryCache.class, factory);
        }
    }


    private void registerQrCodeLoginCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(QrCodeLoginRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(QrCodeLoginMemoryCache.class, factory);
        }
    }

    private void registerVoucherCacheBean(DefaultListableBeanFactory factory) {
        if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
            registerBean(VoucherRedisCache.class, factory);
        }
        if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
            registerBean(VoucherMemoryCache.class, factory);
        }
    }

    private void registerAuthWebFilterBean(DefaultListableBeanFactory factory) {
        final Properties.Bean validator = properties.getAuth().getValidator();
        final Properties.Bean authority = properties.getAuth().getAuthority();
        registerPropertiesBean(validator, factory);
        registerPropertiesBean(authority, factory);
        registerBean(AuthPathMatcher.class, factory);
        registerBean(AuthWebFilter.class, factory);
    }

    private void registerLauncherBean(DefaultListableBeanFactory factory) {
        registerBean(SmsMessageLauncherImpl.class, factory);
        registerBean(EmailMessageLauncherImpl.class, factory);
    }

    /**
     * 注册 bean 服务
     */
    private synchronized void registerBean(Class<?> bc, DefaultListableBeanFactory factory) {
        if (!factory.containsBean(bc.getName())) {
            final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
            beanDefinition.setBeanClass(bc);
            factory.registerBeanDefinition(bc.getName(), beanDefinition);
        }
    }


    /**
     * 注册 bean 服务
     */
    private synchronized void registerPropertiesBean(Properties.Bean bean, DefaultListableBeanFactory factory) {
        try {
            for (final String item : bean.getDepend()) {
                registerBean(Class.forName(item), factory);
            }
            registerBean(Class.forName(bean.getName()), factory);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
