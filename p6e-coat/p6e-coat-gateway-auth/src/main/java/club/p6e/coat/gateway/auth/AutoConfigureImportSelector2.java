package club.p6e.coat.gateway.auth;

import club.p6e.coat.gateway.auth.cache.redis.AuthRedisCache;
import club.p6e.coat.gateway.auth.certificate.AuthCertificateAuthorityHttpCookieCacheImpl;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

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


    public AutoConfigureImportSelector2(ApplicationContext applicationContext, Properties properties) {
        System.out.println("AutoConfigureImportSelector2");
        final AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        final DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) factory;
        final GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(AuthJsonWebTokenCipher.class);
        defaultListableBeanFactory.registerBeanDefinition(AuthJsonWebTokenCipher.class.getName(), beanDefinition);
//        factory.configureBean(yourBean, yourBean.getClass().getName());
    }
}
