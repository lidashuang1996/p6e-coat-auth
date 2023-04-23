package com.darvi.hksi.badminton.lib.utils;

import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * Spring 的帮助类
 * @author lidashuang
 * @version 1.0
 */
public final class SpringUtil {

    /**
     * 全局的 ApplicationContext 对象
     */
    private static ApplicationContext APPLICATION;

    /**
     * 初始化 ApplicationContext 对象
     * @param application ApplicationContext 对象
     */
    public static void init(ApplicationContext application) {
        APPLICATION = application;
    }

    /**
     * 通过 ApplicationContext 对象获取注入在 Spring 里面的 Bean 对象
     * @param tClass Bean 对象类型
     * @param <T> 类型
     * @return Bean 对象
     */
    public static <T> T getBean(Class<T> tClass) {
        return APPLICATION.getBean(tClass);
    }

    public static <T> Map<String, T> getBeans(Class<T> tClass) {
        return APPLICATION.getBeansOfType(tClass);
    }
}
