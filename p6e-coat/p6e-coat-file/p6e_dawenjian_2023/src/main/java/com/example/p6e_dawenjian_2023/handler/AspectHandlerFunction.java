package com.example.p6e_dawenjian_2023.handler;

import com.example.p6e_dawenjian_2023.aspect.Aspect;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class AspectHandlerFunction {

    public Mono<Map<String, Object>> before(Aspect aspect, Map<String, Object> data) {
        if (data == null) {
            data = new HashMap<>();
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
            data = new HashMap<>();
        }
        if (result == null) {
            result = new HashMap<>();
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
