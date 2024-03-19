package club.p6e.coat.auth.message;

import club.p6e.coat.auth.AuthVoucher;
import club.p6e.coat.auth.Properties;
import club.p6e.coat.auth.cache.QrCodeWebSocketAuthCache;
import club.p6e.coat.common.context.ResultContext;
import club.p6e.coat.auth.generator.VoucherGenerator;
import club.p6e.coat.common.utils.GeneratorUtil;
import club.p6e.coat.common.utils.JsonUtil;
import club.p6e.coat.common.utils.SpringUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author lidashuang
 * @version 1.0
 */
@RequestMapping("/ws")
public class WebSocketMessageImpl implements WebSocketMessage {

    /**
     * 注入日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketMessageImpl.class);

    /**
     * 端口
     */
    private int port;

    /**
     * NETTY 对象
     */
    private EventLoopGroup boss = null;
    private EventLoopGroup work = null;

    /**
     * 构造方法初始化
     *
     * @param properties 配置文件对象
     */
    public WebSocketMessageImpl(Properties properties) {
        this.port = properties.getLogin().getQrCode().getWebSocket().getPort();
    }

    @GetMapping("/qrcode/websocket/auth")
    public ResultContext auth(ServerWebExchange exchange) {
        return ResultContext.build(voucher(exchange));
    }

    @Override
    public void startup() {
        WebSocketHeartbeat.init();
        WebSocketMessagePush.init();
        new Thread(() -> netty(port)).start();
    }

    @Override
    public void shutdown() {
        if (boss != null) {
            boss.shutdownGracefully();
            boss = null;
        }
        if (work != null) {
            work.shutdownGracefully();
            work = null;
        }
        WebSocketHeartbeat.close();
        WebSocketMessagePush.close();
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String voucher(ServerWebExchange exchange) {
        final VoucherGenerator generator = SpringUtil.getBean(VoucherGenerator.class);
        final QrCodeWebSocketAuthCache cache = SpringUtil.getBean(QrCodeWebSocketAuthCache.class);
        final String voucher = generator.execute();
        AuthVoucher
                .init(exchange)
                .flatMap(v -> cache.set(voucher, JsonUtil.toJson(new HashMap<>() {{
                    put("voucher", v.getMark());
                }}))).block();
        return voucher;
    }

    @SuppressWarnings("ALL")
    @Override
    public Map<String, Object> cache(String voucher) {
        final QrCodeWebSocketAuthCache cache = SpringUtil.getBean(QrCodeWebSocketAuthCache.class);
        return cache.get(voucher).map(c -> (Map<String, Object>) JsonUtil.fromJson(c, Map.class)).block();
    }

    @Override
    public String push(String content, Function<Map<String, Object>, Boolean> callback) {
        return WebSocketMessagePush.execute(content, callback);
    }

    /**
     * NETTY WebSocket 服务启动
     *
     * @param port 启动的端口
     */
    private void netty(int port) {
        boss = new NioEventLoopGroup();
        work = new NioEventLoopGroup();
        try {
            final WebSocketMessage wsm = this;
            final ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<>() {
                @Override
                protected void initChannel(Channel channel) {
                    // HTTP
                    channel.pipeline().addLast(new HttpServerCodec());
                    // WEBSOCKET
                    channel.pipeline().addLast(new WebSocketServerProtocolHandler(
                            "/ws",
                            null,
                            true,
                            65536,
                            false,
                            true
                    ));
                    // 自定义
                    channel.pipeline().addLast(new Handler(wsm));
                }
            });
            final Channel channel = bootstrap.bind(port).sync().channel();
            // Returns the runtime object associated with the current Java application
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                boss.shutdownGracefully();
                work.shutdownGracefully();
            }));
            LOGGER.info("[ WEBSOCKET SERVICE ] ==> START SUCCESSFULLY... BIND ( " + port + " )");
            channel.closeFuture().sync();
        } catch (Exception e) {
            LOGGER.error("[ WEBSOCKET SERVICE ] ==> ERROR >>>> " + e.getMessage());
        } finally {
            if (boss != null) {
                boss.shutdownGracefully();
                boss = null;
            }
            if (work != null) {
                work.shutdownGracefully();
                work = null;
            }
        }
    }

    /**
     * Netty WebSocket 服务处理器
     */
    private static class Handler implements ChannelInboundHandler {

        /**
         * 注入日志对象
         */
        private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

        /**
         * 登入消息
         */
        private static final String LOGIN_CONTENT = "{\"type\":\"login\"}";

        /**
         * 登出消息
         */
        private static final String LOGOUT_CONTENT = "{\"type\":\"logout\"}";

        /**
         * 心跳消息
         */
        private static final String HEARTBEAT_CONTENT = "{\"type\":\"heartbeat\"}";

        /**
         * 客户端 ID
         */
        private final String id;

        /**
         * Web Socket Message 消息对象
         */
        private final WebSocketMessage webSocketMessage;

        /**
         * 构造方法初始化
         */
        public Handler(WebSocketMessage webSocketMessage) {
            this.id = GeneratorUtil.uuid() + GeneratorUtil.random();
            this.webSocketMessage = webSocketMessage;
        }

        private Map<String, String> getUrlParams(String url) {
            final Map<String, String> params = new HashMap<>();
            if (!(url == null || url.isEmpty())) {
                final int index = url.lastIndexOf("?");
                final String query = url.substring(index + 1);
                final String[] split = query.split("&");
                for (final String item : split) {
                    final String[] kv = item.split("=");
                    if (kv.length == 2) {
                        params.put(kv[0], kv[1]);
                    }
                }
            }
            return params;
        }

        @Override
        public void channelRegistered(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelRegistered");
        }

        @Override
        public void channelUnregistered(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelUnregistered");
        }

        @Override
        public void channelActive(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelActive");
        }

        @Override
        public void channelInactive(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelInactive");
        }

        @Override
        public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) {
            if (o instanceof final TextWebSocketFrame textWebSocketFrame) {
                final String text = textWebSocketFrame.text();
                if (HEARTBEAT_CONTENT.equalsIgnoreCase(text)) {
                    WebSocketSessionManager.refresh(id);
                    channelHandlerContext.writeAndFlush(new TextWebSocketFrame(HEARTBEAT_CONTENT));
                }
            } else if (o instanceof PingWebSocketFrame) {
                channelHandlerContext.writeAndFlush(new PongWebSocketFrame());
            } else if (o instanceof PongWebSocketFrame) {
                channelHandlerContext.writeAndFlush(new PingWebSocketFrame());
            }
            LOGGER.info("[ " + id + " ] ==> channelRead, msg: " + o.getClass());
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelReadComplete");
        }

        @Override
        public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) {
            try {
                String voucher;
                if (o instanceof final WebSocketServerProtocolHandler.HandshakeComplete complete) {
                    final Map<String, String> params = getUrlParams(complete.requestUri());
                    if (!params.isEmpty()) {
                        voucher = params.get("v");
                        if (voucher == null) {
                            voucher = params.get("voucher");
                        }
                        if (voucher == null) {
                            // disconnect
                            channelHandlerContext.close();
                        } else {
                            final Map<String, Object> data = webSocketMessage.cache(voucher);
                            WebSocketSessionManager.register(id, data, channelHandlerContext);
                            channelHandlerContext.writeAndFlush(new TextWebSocketFrame(LOGIN_CONTENT));
                        }
                    }
                } else {
                    // disconnect
                    channelHandlerContext.close();
                }
                LOGGER.info("[ " + id + " ] ==> userEventTriggered, msg: " + o.getClass());
            } catch (Exception e) {
                // disconnect
                channelHandlerContext.close();
                LOGGER.error("[ " + id + " ] ==> userEventTriggered, msg: " + o.getClass());
            }
        }

        @Override
        public void channelWritabilityChanged(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> channelWritabilityChanged");
        }

        @Override
        public void handlerAdded(ChannelHandlerContext channelHandlerContext) {
            LOGGER.info("[ " + id + " ] ==> handlerAdded");
        }

        @Override
        public void handlerRemoved(ChannelHandlerContext channelHandlerContext) {
            WebSocketSessionManager.unregister(id);
            channelHandlerContext.writeAndFlush(new TextWebSocketFrame(LOGOUT_CONTENT));
            LOGGER.info("[ " + id + " ] ==> disconnect");
            LOGGER.info("[ " + id + " ] ==> handlerRemoved");
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
            channelHandlerContext.close();
            LOGGER.info("[ " + id + " ] ==> exceptionCaught " + throwable.getMessage());
        }

    }

}
