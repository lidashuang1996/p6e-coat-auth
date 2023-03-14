package com.example.p6e_dawenjian_2023.router;

import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

/**
 * 基础路由函数
 *
 * @author lidashuang
 * @version 1.0
 */
public class BaseRouterFunction implements RouterFunction<ServerResponse> {

    /**
     * 请求谓词对象
     */
    private RequestPredicate predicate;

    /**
     * 处理函数对象
     */
    private HandlerFunction<ServerResponse> handlerFunction;

    /**
     * 构造方法初始化
     *
     * @param predicate 请求谓词对象
     */
    public BaseRouterFunction(RequestPredicate predicate) {
        this.predicate = predicate;
        this.handlerFunction = request -> ServerResponse.ok().build();
    }

    /**
     * 构造方法初始化
     *
     * @param predicate       请求谓词对象
     * @param handlerFunction 处理函数对象
     */
    public BaseRouterFunction(RequestPredicate predicate, HandlerFunction<ServerResponse> handlerFunction) {
        this.predicate = predicate;
        this.handlerFunction = handlerFunction;
    }

    /**
     * 请求路由
     *
     * @param request ServerRequest 对象
     * @return ServerResponse 对象
     */
    @Override
    public Mono<HandlerFunction<ServerResponse>> route(ServerRequest request) {
        if (this.predicate.test(request)) {
            return Mono.just(this.handlerFunction);
        } else {
            return Mono.empty();
        }
    }

    @Override
    public void accept(RouterFunctions.Visitor visitor) {
        visitor.route(this.predicate, this.handlerFunction);
    }

    /**
     * 设置请求谓词对象
     *
     * @param predicate 请求谓词对象
     */
    public void setPredicate(RequestPredicate predicate) {
        this.predicate = predicate;
    }

    /**
     * 设置处理函数对象
     *
     * @param handlerFunction 处理函数对象
     */
    public void setHandlerFunction(HandlerFunction<ServerResponse> handlerFunction) {
        this.handlerFunction = handlerFunction;
    }
}
