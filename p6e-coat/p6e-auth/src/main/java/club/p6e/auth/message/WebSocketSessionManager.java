package club.p6e.auth.message;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author lidashuang
 * @version 1.0
 */
final class WebSocketSessionManager {

    @Data
    @EqualsAndHashCode(callSuper = true)
    public static class Model extends ConcurrentHashMap<String, Object> implements Serializable {
        private String id;
        private long date;
        private ChannelHandlerContext context;

        public Model(
                String id,
                long date,
                Map<String, Object> data,
                ChannelHandlerContext context

        ) {
            super(data);
            this.id = id;
            this.date = date;
            this.context = context;
        }
    }

    private static final Map<String, Model> CACHE = new ConcurrentHashMap<>();

    public static Iterator<Model> all() {
        return CACHE.values().iterator();
    }

    public static Model get(String id) {
        return CACHE.get(id);
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

    public static void refresh(String id) {
        final Model value = CACHE.get(id);
        if (value != null) {
            value.setDate(System.currentTimeMillis());
        }
    }

    public static void register(
            String id,
            Map<String, Object> data,
            ChannelHandlerContext context
    ) {
        CACHE.put(id, new Model(id, System.currentTimeMillis(), data, context));
    }

    public static void unregister(String id) {
        CACHE.remove(id);
    }

}
