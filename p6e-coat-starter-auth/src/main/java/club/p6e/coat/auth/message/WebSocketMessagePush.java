package club.p6e.coat.auth.message;

import club.p6e.coat.common.utils.GeneratorUtil;
import club.p6e.coat.common.utils.JsonUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 核心推送
 *
 * @author lidashuang
 * @version 1.0
 */
final class WebSocketMessagePush {

    /**
     * 推送的线程池长度
     */
    private static int THREAD_LENGTH = 10;

    /**
     * 默认的线程池长度
     */
    private static final int DEFAULT_THREAD_LENGTH = 10;

    /**
     * 推送的线程池对象
     */
    private static ThreadPoolExecutor EXECUTOR = null;

    /**
     * 初始化线程池对象
     */
    public synchronized static void init() {
        init(DEFAULT_THREAD_LENGTH);
    }

    /**
     * 初始化线程池对象
     *
     * @param threadPoolLength 线程池大小的长度
     */
    public synchronized static void init(int threadPoolLength) {
        if (EXECUTOR == null) {
            THREAD_LENGTH = threadPoolLength;
            EXECUTOR = new ThreadPoolExecutor(
                    threadPoolLength,
                    threadPoolLength,
                    0L,
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );
        }
    }

    /**
     * 关闭线程池对象
     */
    public synchronized static void close() {
        if (EXECUTOR != null) {
            EXECUTOR.shutdown();
            EXECUTOR = null;
        }
    }

    /**
     * 执行推送消息
     *
     * @param content 消息内容
     */
    public static String execute(String content, Function<Map<String, Object>, Boolean> callback) {
        final String id = GeneratorUtil.uuid();
        EXECUTOR.submit(() -> {
            final List<List<WebSocketSessionManager.Model>> balance =
                    executeLoadBalancing(WebSocketSessionManager.list(callback));
            for (final List<WebSocketSessionManager.Model> models : balance) {
                EXECUTOR.submit(new TRunnable(id, content, models));
            }
        });
        return id;
    }

    /**
     * 执行获取需要发送的客户端会话对象列表
     *
     * @param list 获取全部的客户端会话对象列表
     * @return 需要发送的客户端会话对象分组列表
     */
    private static List<List<WebSocketSessionManager.Model>> executeLoadBalancing(List<WebSocketSessionManager.Model> list) {
        final int size = list.size();
        final List<List<WebSocketSessionManager.Model>> result = new ArrayList<>();
        if (size > 0) {
            for (int i = 0; i < THREAD_LENGTH; i++) {
                result.add(new ArrayList<>());
            }
            for (int i = 0; i < size; i = i + THREAD_LENGTH) {
                for (int j = 0; j < THREAD_LENGTH; j++) {
                    if (i + j < size) {
                        result.get(j).add(list.get(i + j));
                    }
                }
            }
        }
        return result;
    }


    /**
     * 推送的线程对象
     */
    @SuppressWarnings("all")
    private static class TRunnable implements Runnable {
        /**
         * 消息的ID
         */
        private final String id;

        /**
         * 消息的内容
         */
        private final String content;

        /**
         * 客户端列表对象
         */
        private final List<WebSocketSessionManager.Model> list;

        /**
         * 构造方法初始化
         *
         * @param id      消息的ID
         * @param content 消息的内容
         * @param list    客户端列表对象
         */
        public TRunnable(String id, String content, List<WebSocketSessionManager.Model> list) {
            this.id = id;
            this.list = list;
            this.content = content;
        }

        @Override
        public void run() {
            final Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            map.put("content", content);
            map.put("type", "websocket_event");
            final String message = JsonUtil.toJson(map);
            for (final WebSocketSessionManager.Model model : list) {
                model.getContext().writeAndFlush(new TextWebSocketFrame(message));
            }
        }
    }


}
