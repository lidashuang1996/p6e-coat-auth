package club.p6e.auth.validator;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 参数验证器接口
 *
 * @author lidashuang
 * @version 1.0
 */
public interface ParameterValidatorInterface {

    /**
     * 执行顺序，越小越先执行
     *
     * @return 排序
     */
    public int order();

    /**
     * 选择验证对象的类型
     *
     * @return 验证对象的类型
     */
    public Class<?> select();

    /**
     * 执行验证
     *
     * @param data    待验证数据的对象
     * @return 验证结果
     */
    public Mono<Boolean> execute(ServerWebExchange exchange, Object data);

}
