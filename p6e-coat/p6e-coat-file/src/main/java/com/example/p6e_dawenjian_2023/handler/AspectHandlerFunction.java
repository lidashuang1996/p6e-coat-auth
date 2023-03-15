package com.example.p6e_dawenjian_2023.handler;

import com.example.p6e_dawenjian_2023.aspect.Aspect;
import lombok.Data;
import lombok.experimental.Accessors;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AspectHandlerFunction {


    @Data
    @Accessors(chain = true)
    public static final class ResultContext implements Serializable {

        /**
         * 默认的状态码
         */
        private static final int DEFAULT_CODE = 0;

        /**
         * 默认的消息内容
         */
        private static final String DEFAULT_MESSAGE = "SUCCESS";

        /**
         * 默认的数据内容
         */
        private static final String DEFAULT_DATA = null;

        /**
         * 状态码
         */
        private Integer code;

        /**
         * 消息
         */
        private String message;

        /**
         * 数据的对象
         */
        private Object data;

        /**
         * 编译方法
         *
         * @return 结果上下文对象
         */
        public static ResultContext build() {
            return new ResultContext(DEFAULT_CODE, DEFAULT_MESSAGE, DEFAULT_DATA);
        }

        /**
         * 编译方法
         *
         * @param data 数据的对象
         * @return 结果上下文对象
         */
        public static ResultContext build(Object data) {
            return new ResultContext(DEFAULT_CODE, DEFAULT_MESSAGE, data);
        }

        /**
         * 编译方法
         *
         * @param code    消息状态码
         * @param message 消息内容
         * @param data    数据的对象
         * @return 结果上下文对象
         */
        public static ResultContext build(Integer code, String message, Object data) {
            return new ResultContext(code, message, data);
        }

        /**
         * 构造方法初始化
         *
         * @param code    状态码
         * @param message 消息
         * @param data    数据的对象
         */
        private ResultContext(Integer code, String message, Object data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    public Mono<Map<String, Object>> before(Aspect aspect, Map<String, Object> data) {
        if (data == null) {
            data = new HashMap<>(0);
        }
        final Map<String, Object> finalData = data;
        return aspect.before(finalData)
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(finalData);
                    } else {
                        return Mono.error(new RuntimeException());
                    }
                });
    }

    public Mono<Map<String, Object>> after(Aspect aspect, Map<String, Object> data, Map<String, Object> result) {
        if (data == null) {
            data = new HashMap<>(0);
        }
        if (result == null) {
            result = new HashMap<>(0);
        }
        final Map<String, Object> finalData = data;
        final Map<String, Object> finalResult = result;
        return aspect.after(finalData, finalResult)
                .flatMap(b -> {
                    if (b) {
                        return Mono.just(finalResult);
                    } else {
                        return Mono.error(new RuntimeException());
                    }
                });
    }

}
