package club.p6e.coat.gateway.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @author lidashuang
 * @version 1.0
 */
public class IpUtil {

    private static Interface SERVER = new Impl();

    public static void setContent(Interface server) {
        SERVER = server;
    }

    public static String obtain(ServerHttpRequest request) {
        return SERVER.obtain(request);
    }

    interface Interface {

        /**
         * 获取 IP 地址
         *
         * @param request ServerHttpRequest 对象
         * @return IP 地址
         */
        public String obtain(ServerHttpRequest request);

    }

    public static class Impl implements Interface {

        /**
         * 本机 IP
         */
        private static final String LOCAL_IP = "127.0.0.1";

        /**
         * 未知 IP
         */
        private final static String IP_UNKNOWN = "unknown";

        /**
         *
         */
        private static final String IP_HEADER_X_REQUEST_IP = "x-request-ip";

        /**
         * IP 请求头
         */
        private static final String IP_HEADER_X_FORWARDED_FOR = "x-forwarded-for";

        /**
         * IP 请求头
         */
        private static final String IP_HEADER_PROXY_CLIENT_IP = "proxy-client-ip";

        /**
         * IP 请求头
         */
        private static final String IP_HEADER_WL_PROXY_CLIENT_IP = "wl-proxy-client-ip";

        @Override
        public String obtain(ServerHttpRequest request) {
            final HttpHeaders httpHeaders = request.getHeaders();
            List<String> list = httpHeaders.get(IP_HEADER_X_FORWARDED_FOR);
            if (list == null || list.size() == 0 || IP_UNKNOWN.equalsIgnoreCase(list.get(0))) {
                list = httpHeaders.get(IP_HEADER_PROXY_CLIENT_IP);
            }
            if (list == null || list.size() == 0 || IP_UNKNOWN.equalsIgnoreCase(list.get(0))) {
                list = httpHeaders.get(IP_HEADER_WL_PROXY_CLIENT_IP);
            }
            if (list == null || list.size() == 0 || IP_UNKNOWN.equalsIgnoreCase(list.get(0))) {
                list = httpHeaders.get(IP_HEADER_X_REQUEST_IP);
            }
            if (list == null || list.size() == 0 || IP_UNKNOWN.equalsIgnoreCase(list.get(0))) {
                final InetSocketAddress inetSocketAddress = request.getRemoteAddress();
                if (inetSocketAddress != null
                        && inetSocketAddress.getAddress() != null
                        && inetSocketAddress.getAddress().getHostAddress() != null) {
                    final String inetSocketAddressHost = inetSocketAddress.getAddress().getHostAddress();
                    if (LOCAL_IP.equals(inetSocketAddressHost)) {
                        try {
                            list = List.of(InetAddress.getLocalHost().getHostAddress());
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                    } else {
                        list = List.of(inetSocketAddressHost);
                    }
                }
            }
            if (list != null && list.size() > 0) {
                return list.get(0);
            }
            return IP_UNKNOWN;
        }

    }

}
