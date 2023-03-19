package club.p6e.coat.file.context;

import org.springframework.web.reactive.function.server.ServerRequest;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidashuang
 * @version 1.0
 */
public class ResourceContext extends HashMap<String, Object> implements Serializable {

    /**
     * 下载的文件节点
     */
    private String node;

    /**
     * 下载的文件路径
     */
    private String path;

    private ServerRequest serverRequest;

    public ServerRequest getServerRequest() {
        return serverRequest;
    }

    public void setServerRequest(ServerRequest serverRequest) {
        this.serverRequest = serverRequest;
    }

    /**
     * 无参数构造
     */
    public ResourceContext() {
    }

    /**
     * map 初始化构造
     *
     * @param map 初始化对象
     */
    public ResourceContext(Map<String, Object> map) {
        this.putAll(map);
        if (map.get("node") != null && map.get("node") instanceof final String content) {
            this.setNode(content);
        }
        if (map.get("path") != null && map.get("path") instanceof final String content) {
            this.setPath(content);
        }
    }

    public String getNode() {
        return node;
    }

    public void setNode(String node) {
        this.node = node;
        this.put("node", node);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        this.put("path", path);
    }

    public Map<String, Object> toMap() {
        return this;
    }

}

