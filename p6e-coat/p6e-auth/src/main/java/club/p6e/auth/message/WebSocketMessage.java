package club.p6e.auth.message;

import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.function.Function;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface WebSocketMessage {

    /**
     * 启动服务
     */
    public void startup();

    /**
     * 关闭服务
     */
    public void shutdown();

    /**
     * 获取端口
     *
     * @return 端口
     */
    public int getPort();

    /**
     * 设置端口
     *
     * @param port 端口
     */
    public void setPort(int port);

    /**
     * 缓存数据
     *
     * @param voucher 凭证
     * @return 缓存的数据
     */
    public Map<String, Object> cache(String voucher);

    /**
     * 凭证
     *
     * @param exchange ServerWebExchange 对象
     * @return 凭证类型
     */
    public String voucher(ServerWebExchange exchange);

    /**
     * 推送消息
     *
     * @param content  消息内容
     * @param callback 条件函数
     * @return 消息 ID
     */
    public String push(String content, Function<Map<String, Object>, Boolean> callback);

}
