package club.p6e.auth;

/**
 * @author lidashuang
 * @version 1.0
 */
public interface Configuration {

    /**
     * 自定义的配置
     *
     * @param properties 配置对象
     */
    public void execute(Properties properties);

}
