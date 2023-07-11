package club.p6e.coat.gateway.auth;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
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

        System.out.println(
                applicationContext.getAutowireCapableBeanFactory()
        );

        final AutowireCapableBeanFactory factory = applicationContext.getAutowireCapableBeanFactory();
        AuthRedisCache yourBean = factory.configureBean(AuthRedisCache.class);
        autowireCapableBeanFactory.autowireBean(yourBean);

    }
}
