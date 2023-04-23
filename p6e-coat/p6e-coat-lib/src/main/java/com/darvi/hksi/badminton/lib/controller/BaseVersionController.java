package com.darvi.hksi.badminton.lib.controller;

import com.darvi.hksi.badminton.lib.Properties;
import com.darvi.hksi.badminton.lib.context.ResultContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 基础的 controller 类
 *
 * @author lidashuang
 * @version 1.0
 */
@RestController
@RequestMapping("/version")
@Component(BaseVersionController.BEAN_NAME)
public class BaseVersionController extends BaseController {

    /**
     * 注入的 BEAN 的名称
     */
    public static final String BEAN_NAME = "com.darvi.hksi.badminton.lib.controller.BaseVersionController";

    /**
     * 配置文件对象
     */
    private final Properties properties;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public BaseVersionController(Properties properties) {
        this.properties = properties;
    }

    @RequestMapping("")
    public Object def1() {
        return def2();
    }

    @RequestMapping("/")
    @SuppressWarnings("all")
    public Object def2() {
        try {
            Class.forName("jakarta.servlet.ServletRequest");
            return ResultContext.build(properties.getVersion());
        } catch (Exception e) {
            return Mono.just(ResultContext.build(properties.getVersion()));
        }
    }

}
