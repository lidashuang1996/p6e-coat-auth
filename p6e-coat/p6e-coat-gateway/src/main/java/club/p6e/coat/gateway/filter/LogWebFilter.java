package club.p6e.coat.gateway.filter;

import club.p6e.coat.gateway.WebFilterOrder;
import club.p6e.coat.gateway.utils.IpUtil;
import club.p6e.coat.gateway.utils.JsonUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义日志过滤器
 *
 * @author lidashuang
 * @version 1.0
 */
@Component
public class LogWebFilter implements WebFilter, Ordered {

    /**
     * 执行顺序
     */
    protected static final WebFilterOrder ORDER = WebFilterOrder.LOG_FILTER;

    /**
     * 日志对象
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LogWebFilter.class);

    /**
     * DATA BUFFER 工厂
     */
    protected static final DataBufferFactory DATA_BUFFER_FACTORY = new DefaultDataBufferFactory();

    /**
     * 用户的头名称
     */
    private static String USER_INFO_HEADER = "P6e-User-Info";

    /**
     * 日志回调
     */
    private LogCallback logCallback = model -> {
        LOGGER.info(model.toString());
        return Mono.just(model);
    };

    /**
     * 设置用户信息头
     *
     * @param content 用户信息头
     */
    public static void setUserInfoHeader(String content) {
        USER_INFO_HEADER = content;
    }

    /**
     * 设置日志回调
     *
     * @param logCallback 日志回调对象
     */
    public void setCallback(LogCallback logCallback) {
        this.logCallback = logCallback;
    }

    @Override
    public int getOrder() {
        return ORDER.getOrder();
    }

    @NonNull
    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        // 创建日志模型
        final Model model = new Model();
        // 请求日志处理
        final ServerHttpRequest request = new LogServerHttpRequestDecorator(exchange, model);
        // 结果日志处理
        final ServerHttpResponse response = new LogServerHttpResponseDecorator(exchange, model, USER_INFO_HEADER, logCallback);
        // 继续执行
        return chain.filter(exchange.mutate().request(request).response(response).build());
    }

    /**
     * 日志回调
     */
    public interface LogCallback {

        /**
         * 执行日志回调
         *
         * @param model 日志模型
         * @return Mono/Model 日志模型
         */
        public Mono<Model> execute(Model model);

    }

    /**
     * 日志 Request 解码器
     */
    public static class LogServerHttpRequestDecorator extends ServerHttpRequestDecorator {

        /**
         * 日志模型对象
         */
        private final Model model;

        /**
         * Request 对象
         */
        private final ServerHttpRequest request;

        /**
         * 构造方法初始化
         *
         * @param exchange ServerWebExchange 对象
         * @param model    对象
         */
        public LogServerHttpRequestDecorator(ServerWebExchange exchange, Model model) {
            super(exchange.getRequest());
            final ServerHttpRequest request = exchange.getRequest();
            this.request = request;
            model.setId(request.getId());
            model.setIp(IpUtil.obtain(request));
            model.setPath(request.getPath().value());
            model.setRequestDateTime(System.currentTimeMillis());
            model.setRequestMethod(request.getMethod().name());
            model.setRequestCookies(JsonUtil.toJson(request.getCookies()));
            model.setRequestHeaders(JsonUtil.toJson(request.getHeaders()));
            model.setRequestQueryParams(JsonUtil.toJson(request.getQueryParams()));
            this.model = model;
        }

        @NonNull
        @Override
        public Flux<DataBuffer> getBody() {
            final Map<String, String> rbm = new HashMap<>(3);
            final List<String> types = request.getHeaders().get(HttpHeaders.CONTENT_TYPE);
            final DataBuffer content = DATA_BUFFER_FACTORY.allocateBuffer(1024 * 5);
            if (types == null || types.size() == 0) {
                rbm.put("type", "unknown");
            } else {
                rbm.put("type", types.get(0));
            }
            return DataBufferUtils.join(super.getBody())
                    .map(b -> {
                        final byte[] bytes = new byte[b.readableByteCount()];
                        b.read(bytes);
                        DataBufferUtils.release(b);
                        if (bytes.length <= content.writableByteCount()) {
                            content.write(bytes);
                        } else {
                            final int wl = content.writableByteCount();
                            final byte[] write = new byte[wl];
                            System.arraycopy(write, 0, bytes, 0, wl);
                            content.write(write);
                        }
                        rbm.put("size", String.valueOf(bytes.length));
                        rbm.put("content", new String(content.toByteBuffer().array(), StandardCharsets.UTF_8)
                                .replaceAll("\r", "").replaceAll("\n", ""));
                        model.setRequestBody(JsonUtil.toJson(rbm));
                        return DATA_BUFFER_FACTORY.wrap(bytes);
                    })
                    .flux();
        }
    }

    /**
     * 日志 Response 解码器
     */
    public static class LogServerHttpResponseDecorator extends ServerHttpResponseDecorator {

        /**
         * 日志模型对象
         */
        private final Model model;

        /**
         * Request 对象
         */
        private final ServerHttpRequest request;

        /**
         * Response 对象
         */
        private final ServerHttpResponse response;

        /**
         * 用户信息缓存的请求头
         */
        private final String userInfoHeader;

        /**
         * 日志回调
         */
        private final LogCallback logCallback;

        /**
         * 构造方法初始化
         *
         * @param exchange ServerWebExchange 对象
         * @param model    对象
         * @param header   用户的信息缓存的请求头名称
         */
        public LogServerHttpResponseDecorator(ServerWebExchange exchange, Model model, String header, LogCallback logCallback) {
            super(exchange.getResponse());
            final ServerHttpRequest request = exchange.getRequest();
            final ServerHttpResponse response = exchange.getResponse();
            this.model = model;
            this.request = request;
            this.response = response;
            this.userInfoHeader = header;
            this.logCallback = logCallback;
        }

        @NonNull
        @Override
        public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
            final Map<String, String> rbm = new HashMap<>(3);
            final DataBuffer content = DATA_BUFFER_FACTORY.allocateBuffer(1024 * 5);
            final List<String> types = response.getHeaders().get(HttpHeaders.CONTENT_TYPE);
            if (types == null || types.size() == 0) {
                rbm.put("type", "unknown");
            } else {
                rbm.put("type", types.get(0));
            }
            // ===== USER INFO ========================================
            final List<String> userInfoData = request.getHeaders().get(userInfoHeader);
            if (userInfoData != null) {
                model.setUser(JsonUtil.toJson(userInfoData));
            }
            // ===== USER INFO ========================================
            return super.writeWith(DataBufferUtils.join(body)
                    .flatMap(b -> {
                        final byte[] bytes = new byte[b.readableByteCount()];
                        b.read(bytes);
                        DataBufferUtils.release(b);
                        if (bytes.length > content.writableByteCount()) {
                            final int wl = content.writableByteCount();
                            final byte[] write = new byte[wl];
                            System.arraycopy(write, 0, bytes, 0, wl);
                            content.write(write);
                        } else {
                            content.write(bytes);
                        }
                        rbm.put("size", String.valueOf(bytes.length));
                        rbm.put("content", new String(content.toByteBuffer().array(), StandardCharsets.UTF_8)
                                .replaceAll("\r", "").replaceAll("\n", ""));
                        model.setResponseBody(JsonUtil.toJson(rbm));
                        model.setResponseDateTime(System.currentTimeMillis());
                        model.setResponseHeaders(JsonUtil.toJson(response.getHeaders()));
                        model.setResponseCookies(JsonUtil.toJson(response.getCookies()));
                        model.setIntervalDateTime(model.getResponseDateTime() - model.getRequestDateTime());
                        return logCallback.execute(model).map(m -> DATA_BUFFER_FACTORY.wrap(bytes));
                    }));
        }
    }

    /**
     * 日志模型
     */
    @Data
    @Accessors(chain = true)
    @SuppressWarnings("ALL")
    public static class Model implements Serializable {
        private volatile String id;
        private volatile String path;
        private volatile long requestDateTime;
        private volatile String requestMethod;
        private volatile String requestCookies;
        private volatile String requestHeaders;
        private volatile String requestBody;
        private volatile String requestQueryParams;
        private volatile String responseBody;
        private volatile String responseHeaders;
        private volatile String responseCookies;
        private volatile long responseDateTime;
        private volatile long intervalDateTime;
        private volatile String ip;
        private volatile String user;

        @Override
        public String toString() {
            return JsonUtil.toJson(this);
        }
    }

}
