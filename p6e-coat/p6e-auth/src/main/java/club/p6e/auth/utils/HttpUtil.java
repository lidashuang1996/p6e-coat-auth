package club.p6e.auth.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public final class HttpUtil {

    private static final WebClient WC = WebClient.create();

    public static Mono<String> doGet(String url) {
        return doGet(url, new HashMap<>(), new HashMap<>());
    }

    public static Mono<String> doGet(String url, Map<String, String> headers, Map<String, String> params) {
        if (params != null) {
            final StringBuilder sb = new StringBuilder();
            for (final String key : params.keySet()) {
                sb
                        .append("&")
                        .append(URLEncoder.encode(key, StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(params.get(key), StandardCharsets.UTF_8));
            }
            if (sb.length() > 0) {
                url += "?" + sb.substring(1);
            }
        }
        return WC
                .get()
                .uri(url)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        for (final String key : headers.keySet()) {
                            httpHeaders.set(key, headers.get(key));
                        }
                    }
                })
                .retrieve()
                .toEntity(String.class)
                .flatMap(response -> {
                    final String body = response.getBody();
                    final HttpStatusCode status = response.getStatusCode();
                    if (body == null || status != HttpStatus.OK) {
                        return Mono.empty();
                    } else {
                        return Mono.just(body);
                    }
                });
    }

    public static Mono<String> doPost(String url) {
        return doPost(url, new HashMap<>(), BodyInserters.empty());
    }

    public static Mono<String> doPost(String url, String json) {
        return doPost(url, null, json);
    }

    public static Mono<String> doPost(String url, Map<String, String> headers, String json) {
        if (headers == null) {
            headers = new HashMap<>();
        }
        headers.put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return doPost(url, headers, BodyInserters.fromValue(json));
    }

    public static Mono<String> doPost(String url, Map<String, String> headers, BodyInserter<?, ? super ClientHttpRequest> bodyInserter) {
        return WC
                .post()
                .uri(url)
                .headers(httpHeaders -> {
                    if (headers != null) {
                        for (final String key : headers.keySet()) {
                            httpHeaders.set(key, headers.get(key));
                        }
                    }
                })
                .body(bodyInserter)
                .retrieve()
                .toEntity(String.class)
                .flatMap(response -> {
                    final String body = response.getBody();
                    final HttpStatusCode status = response.getStatusCode();
                    if (body == null || status != HttpStatus.OK) {
                        return Mono.empty();
                    } else {
                        return Mono.just(body);
                    }
                });
    }

}
