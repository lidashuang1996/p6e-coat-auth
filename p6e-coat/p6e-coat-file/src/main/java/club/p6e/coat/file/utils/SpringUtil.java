package club.p6e.coat.file.utils;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class SpringUtil {

    private static ApplicationContext APPLICATION;

    public static void init(ApplicationContext application) {
        APPLICATION = application;
    }

    public static <T> T getBean(Class<T> tClass) {
        return APPLICATION.getBean(tClass);
    }

    public static <T> Map<String, T> getBeans(Class<T> tClass) {
        return APPLICATION.getBeansOfType(tClass);
    }

}
