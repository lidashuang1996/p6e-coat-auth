package club.p6e.auth.message;

import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

/**
 * @author lidashuang
 * @version 1.0
 */
final class WebSocketHeartbeat {

    /**
     * 轮训间隔时间
     */
    private static final long INTERVAL = 60 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketHeartbeat.class);

    /**
     * 心跳的线程
     */
    private static Thread TT = null;
    private static volatile int MARK = 0;

    /**
     * 初始化
     */
    public synchronized static void init() {
        if (TT == null) {
            MARK = 1;
            TT = new TI();
            TT.start();
        }
    }

    /**
     * 关闭
     */
    public synchronized static void close() {
        if (TT != null) {
            MARK = -1;
            new Thread(() -> {
                try {
                    // The length of sleep interval time
                    Thread.sleep(INTERVAL);
                    MARK = 0;
                    TT = null;
                } catch (Exception e) {
                    LOGGER.error("[CLOSE] >> " + e.getMessage(), e);
                }
            }).start();
        }
    }

    /**
     * 心跳处理的线程
     */
    private static class TI extends Thread {

        @SuppressWarnings("all")
        @Override
        public void run() {
            while (true) {
                try {
                    // If the mark is not equal to 1, the thread is closed
                    if (MARK != 1) {
                        return;
                    }
                    // now date
                    final long now = System.currentTimeMillis();
                    // The iteration object of the current client object
                    final Iterator<WebSocketSessionManager.Model> iterator = WebSocketSessionManager.all();
                    // Reading client objects to determine if there are kicked out client objects
                    while (iterator.hasNext()) {
                        try {
                            final WebSocketSessionManager.Model model = iterator.next();
                            final ChannelHandlerContext context = model.getContext();
                            if (context != null) {
                                if (context.isRemoved()) {
                                    iterator.remove();
                                } else if (now - model.getDate() > INTERVAL) {
                                    context.close();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // The length of sleep interval time
                    Thread.sleep(INTERVAL);
                } catch (Exception e) {
                    LOGGER.error("[TI WHILE RUN] >> " + e.getMessage(), e);
                }
            }
        }
    }

}
