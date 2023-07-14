package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.cache.redis.AccountPasswordLoginSignatureRedisCache;
import club.p6e.coat.gateway.auth.cache.redis.AuthRedisCache;
import club.p6e.coat.gateway.auth.cache.redis.VoucherRedisCache;
import club.p6e.coat.gateway.auth.codec.AuthAccountPasswordLoginTransmissionCodecImpl;
import club.p6e.coat.gateway.auth.controller.*;
import club.p6e.coat.gateway.auth.repository.UserAuthRepository;
import club.p6e.coat.gateway.auth.repository.UserRepository;
import club.p6e.coat.gateway.auth.service.*;
import club.p6e.coat.gateway.auth.validator.support.AccountPasswordLoginParameterValidator;
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

    /**
     * 扫描包下面的路径
     */
    private static final String SCAN_PATH = "/**";

    /**
     * 扫描包下面的文件名称
     */
    private static final String SCAN_FILE_NAME = "/**.class";

    private Properties properties;

    public AutoConfigureImportSelector2(
            Properties properties,
            Configuration configuration,
            ApplicationContext applicationContext
    ) {
        System.out.println("AutoConfigureImportSelector2");
        final AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        final DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) factory;

        if (properties.isEnable()) {
            registerBean(AuthPathMatcher.class, defaultListableBeanFactory);
//            registerBean();
            registerBean(AuthWebFilter.class, defaultListableBeanFactory);
        }

        if (properties.getLogin().isEnable()) {
            registerBean(IndexServiceImpl.class, defaultListableBeanFactory);
            registerBean(IndexControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationLoginControllerImpl.class, defaultListableBeanFactory);
            if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
                registerBean(VoucherRedisCache.class, defaultListableBeanFactory);
            }
            if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
                registerBean(VoucherRedisCache.class, defaultListableBeanFactory);
            }
        }

        if (properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()) {
            registerAuthCacheBean(defaultListableBeanFactory);
            registerRepositoryBean(defaultListableBeanFactory);
            registerBean(AuthPasswordEncryptorImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(AccountPasswordLoginParameterValidator.class, defaultListableBeanFactory);

        }

        if (properties.getLogin().isEnable()
                && properties.getLogin().getAccountPassword().isEnable()
                && properties.getLogin().getAccountPassword().isEnableTransmissionEncryption()) {
            registerBean(AccountPasswordLoginSignatureControllerImpl.class, defaultListableBeanFactory);
            registerBean(AuthAccountPasswordLoginTransmissionCodecImpl.class, defaultListableBeanFactory);
            if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
                registerBean(AccountPasswordLoginSignatureRedisCache.class, defaultListableBeanFactory);
            }
            if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
                registerBean(AccountPasswordLoginSignatureRedisCache.class, defaultListableBeanFactory);
            }
        }

        if (properties.getLogin().isEnable()
                && properties.getLogin().getVerificationCode().isEnable()) {
            registerAuthCacheBean(defaultListableBeanFactory);
            registerRepositoryBean(defaultListableBeanFactory);
            registerBean(VerificationCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeLoginControllerImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(VerificationCodeObtainControllerImpl.class, defaultListableBeanFactory);
        }

        if (properties.getLogin().isEnable()
                && properties.getLogin().getQrCode().isEnable()) {
            registerAuthCacheBean(defaultListableBeanFactory);
            registerRepositoryBean(defaultListableBeanFactory);
            registerBean(QrCodeLoginServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeLoginControllerDefaultImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainServiceImpl.class, defaultListableBeanFactory);
            registerBean(QrCodeObtainControllerImpl.class, defaultListableBeanFactory);
        }
    }

    private void registerBean(Class<?> beanClass, DefaultListableBeanFactory factory) {
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(beanClass);
        factory.registerBeanDefinition(beanClass.getName(), beanDefinition);
    }

    private boolean isRegisterRepository = false;

    private synchronized void registerRepositoryBean(DefaultListableBeanFactory factory) {
        if (!isRegisterRepository) {
            registerBean(UserRepository.class, factory);
            registerBean(AuthUserDetails.class, factory);
            registerBean(UserAuthRepository.class, factory);
            isRegisterRepository = true;
        }
    }

    private boolean isRegisterAuthCache = false;

    private synchronized void registerAuthCacheBean(DefaultListableBeanFactory factory) {
        if (!isRegisterAuthCache) {
            if (properties.getCache().getType() == Properties.Cache.Type.REDIS) {
                registerBean(AuthRedisCache.class, factory);
            }
            if (properties.getCache().getType() == Properties.Cache.Type.MEMORY) {
                registerBean(AuthRedisCache.class, factory);
            }
            isRegisterAuthCache = true;
        }
    }

}
