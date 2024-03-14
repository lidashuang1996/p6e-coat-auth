package club.p6e.coat.auth.generator;

/**
 * 第三方登录的 state 生成器
 *
 * @author lidashuang
 * @version 1.0
 */
public interface StateOtherLoginGenerator {

    /**
     * 生成码
     *
     * @return 码
     */
    public String execute();

}
