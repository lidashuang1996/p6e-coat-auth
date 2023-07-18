package club.p6e.coat.gateway.auth.service;

import club.p6e.coat.gateway.auth.AuthVoucher;
import club.p6e.coat.gateway.auth.utils.TemplateParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class IndexServiceImpl implements IndexService {

    /**
     * 默认的模板内容
     */
    private String templateContent = "@{voucher}";

    /**
     * 默认的媒体类型
     */
    private MediaType mediaType = MediaType.TEXT_HTML;

    @Override
    public void setContentType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    @Override
    public void setTemplateContent(String content) {
        this.templateContent = content;
    }

    @Override
    public Mono<Void> execute(ServerWebExchange exchange, Map<String, String> vm) {
        return AuthVoucher
                .create(vm)
                .flatMap(voucher -> write(
                        exchange,
                        TemplateParser.execute(
                                templateContent,
                                "voucher",
                                voucher.getMark()
                        )
                ));
    }

    /**
     * 写入返回数据
     *
     * @param exchange ServerWebExchange 对象
     * @param content  写入的数据内容
     * @return Mono/Void 对象
     */
    protected Mono<Void> write(ServerWebExchange exchange, String content) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(mediaType);
        return response.writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(content.getBytes())));
    }

}
