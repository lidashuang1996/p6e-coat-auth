package club.p6e.auth.message;

import org.springframework.web.server.ServerWebExchange;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface WebSocketMessage {

    public void startup();

    public void shutdown();

    public int getPort();

    public void setPort(int port);

    public Map<String, Object> cache(String voucher);

    public String voucher(ServerWebExchange exchange);

    public String push(String content, Function<Map<String, Object>, Boolean> callback);

}
