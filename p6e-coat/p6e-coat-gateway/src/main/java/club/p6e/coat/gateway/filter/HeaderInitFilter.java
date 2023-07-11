//package club.p6e.coat.gateway.filter;
//
//import club.p6e.coat.gateway.WebFilterOrder;
//import org.springframework.core.Ordered;
//import org.springframework.lang.NonNull;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.concurrent.CopyOnWriteArrayList;
//
///**
// * Header Init 过滤器
// *
// * @author lidashuang
// * @version 1.0
// */
////@Component
//public class HeaderInitFilter implements WebFilter, Ordered {
//
//    /**
//     * 执行顺序
//     */
//    protected static final WebFilterOrder ORDER = WebFilterOrder.HEADER_INIT_FILTER;
//
//    /**
//     * 需要删除的头部名称前缀
//     */
//    protected List<String> list = new CopyOnWriteArrayList<>();
//
//    /**
//     * 构造方法初始化
//     */
//    public HeaderInitFilter() {
//        this.set("P6e-");
//    }
//
//    @Override
//    public int getOrder() {
//        return ORDER.getOrder();
//    }
//
//    @NonNull
//    @Override
//    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
//        return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().headers(httpHeaders -> {
//            for (final String key : httpHeaders.keySet()) {
//                for (final String item : list) {
//                    if (key.toLowerCase().startsWith(item.toLowerCase())) {
//                        httpHeaders.remove(key);
//                        break;
//                    }
//                }
//            }
//        }).build()).build());
//    }
//
//    /**
//     * 获取需要删除的头部名称前缀列表
//     *
//     * @return 删除的头部名称前缀列表
//     */
//    public List<String> getList() {
//        return list;
//    }
//
//    /**
//     * 添加删除的头部名称前缀
//     */
//    public void set(String content) {
//        this.list.add(content);
//    }
//
//    /**
//     * 移除删除的头部名称前缀
//     */
//    public void remove(String content) {
//        this.list.remove(content);
//    }
//
//}
