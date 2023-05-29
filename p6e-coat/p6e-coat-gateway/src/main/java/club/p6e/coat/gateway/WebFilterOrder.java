package club.p6e.coat.gateway;

/**
 * @author lidashuang
 * @version 1.0
 */
public enum WebFilterOrder {

    FIRST_FILTER(-10000),
    EXCEPTION_FILTER(-9000),
    LOG_FILTER(-8000),
    REFERER(-7000),
    MULTIPLE_CROSS_DOMAIN_FILTER(-6000),
    CROSS_DOMAIN_FILTER(-5000),
    HEADER_INIT_FILTER(-4000),
    AUTH_FILTER(-3000),
    PERMISSION_FILTER(-2000),
    RATE_LIMITING_FILTER(-1000),
    LAST_FILTER(0);

    private final int order;

    WebFilterOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

}
