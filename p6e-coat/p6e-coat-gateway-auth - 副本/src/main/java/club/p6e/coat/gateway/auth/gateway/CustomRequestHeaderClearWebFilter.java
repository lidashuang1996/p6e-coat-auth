//package club.p6e.coat.gateway.auth.gateway;
//
//import org.springframework.core.Ordered;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
///**
// * 自定义请求头清除过滤器
// *
// * @author lidashuang
// * @version 1.0
// */
//@Component
//public class CustomRequestHeaderClearWebFilter implements WebFilter, Ordered {
//    /**
//     * 执行顺序
//     */
//    private static final int ORDER = -2800;
//
//    /**
//     * 重置默认的数据内容
//     */
//    private static final String NULL_CONTENT = "";
//
//    /**
//     * 需要重置的请求数据头
//     */
//    private static final String[] REQUEST_HEADER_FILTERED = new String[]{
//            "User",
//            "Voucher"
//    };
//
//    /**
//     * 配置文件对象
//     */
//    private final Properties properties;
//
//    /**
//     * 构造方法初始化
//     *
//     * @param properties 配置文件对象
//     */
//    public CustomRequestHeaderClearWebFilter(Properties properties) {
//        this.properties = properties;
//    }
//
//    @Override
//    public int getOrder() {
//        return ORDER;
//    }
//
//    @NonNull
//    @Override
//    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
//        final ServerHttpRequest request = exchange.getRequest();
//        final ServerHttpRequest.Builder requestBuilder = request.mutate();
//        // 删除关键的头
//        // 这个请求是内部自己用来调用的
//        // 禁止请求发送携带这个请求头到下游服务
//        final String headerPrefix = properties.getHeaderPrefix();
//        for (final String name : REQUEST_HEADER_FILTERED) {
//            requestBuilder.header(headerPrefix + name, NULL_CONTENT);
//        }
//        return chain.filter(exchange.mutate().request(requestBuilder.build()).build());
//    }
//}
