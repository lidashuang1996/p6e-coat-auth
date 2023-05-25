package club.p6e.coat.gateway.auth.service;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 主页的控制器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface IndexService {

    /**
     * 设置返回类型头
     *
     * @param mediaType 媒体类型
     */
    public void setContentType(MediaType mediaType);

    /**
     * 设置模板内容
     *
     * @param content 模板内容
     */
    public void setTemplateContent(String content);

    public Mono<Void> execute(ServerWebExchange exchange, Map<String, String> vm);

}
