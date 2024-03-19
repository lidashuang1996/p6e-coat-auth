package club.p6e.coat.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.reactive.context.AnnotationConfigReactiveWebServerApplicationContext;

/**
 * http://127.0.0.1:7322/oauth2/authorize?responseType=code&clientId=123456&redirectUri=http://127.0.0.1:9999/cb&scope=open_id,user_info&state=898989
 *
 * @author lidashuang
 * @version 1.0.0
 */
@SpringBootApplication
public class P6eAuthApplication {

    public static void main(String[] args) {
        // 创建 SpringApplication 实例
        SpringApplication application = new SpringApplication(P6eAuthApplication.class);

        // 注册自定义的 ApplicationContextInitializer
        application.addInitializers(new BBB());

        // 运行 SpringApplication
        application.run(args);
    }

}
