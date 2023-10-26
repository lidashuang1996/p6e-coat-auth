package club.p6e.auth.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Web Socket Session Manager
 *
 * @author lidashuang
 * @version 1.0
 */
final class WebSocketSessionManager {

    /**
     * 模型对象
     */
    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Model extends ConcurrentHashMap<String, Object> implements Serializable {
        private String id;
        private long createDate;
        private long updateDate;
        private ChannelHandlerContext context;

        /**
         * 构造方法初始化
         *
         * @param id         会话 ID
         * @param createDate 会话创建时间
         * @param updateDate 会话更新时间
         * @param data       会话数据
         * @param context    会话上下文
         */
        public Model(
                String id,
                long createDate,
                long updateDate,
                Map<String, Object> data,
                ChannelHandlerContext context

        ) {
            super(data);
            this.id = id;
            this.context = context;
            this.createDate = createDate;
            this.updateDate = updateDate;
        }
    }

    /**
     * 缓存对象
     */
    private static final Map<String, Model> CACHE = new ConcurrentHashMap<>();

    /**
     * 获取会话对象
     *
     * @param id 会话 ID
     * @return 会话对象
     */
    public static Model get(String id) {
        return CACHE.get(id);
    }

    /**
     * 获取所有的会话对象
     *
     * @return 会话对象的迭代器
     */
    public static Iterator<Model> all() {
        return CACHE.values().iterator();
    }


    public static List<Model> list(Function<Map<String, Object>, Boolean> callback) {
        final List<Model> list = new ArrayList<>();
        final Iterator<Model> iterator = all();
        while (iterator.hasNext()) {
            final Model model = iterator.next();
            if (callback.apply(model)) {
                list.add(model);
            }
        }
        return list;
    }

    /**
     * 刷新会话更新时间
     *
     * @param id 会话 ID
     */
    public static void refresh(String id) {
        final Model value = CACHE.get(id);
        if (value != null) {
            value.setUpdateDate(System.currentTimeMillis());
        }
    }

    /**
     * 注册的会话
     *
     * @param id 会话 ID
     */
    public static void register(
            String id,
            Map<String, Object> data,
            ChannelHandlerContext context
    ) {
        CACHE.put(id, new Model(id, System.currentTimeMillis(), System.currentTimeMillis(), data, context));
    }

    /**
     * 卸载注册的会话
     *
     * @param id 会话 ID
     */
    public static void unregister(String id) {
        CACHE.remove(id);
    }

}
