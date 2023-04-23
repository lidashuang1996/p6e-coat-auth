package com.darvi.hksi.badminton.lib.order;

/**
 * 排序接口
 *
 * @author lidashuang
 * @version 1.0
 */
public interface Order<T> {

    /**
     * 执行分页
     * T 结果的类型
     *
     * @param orders 排序规则
     * @return 排序规则解析后的对象
     */
    T execute(String[] orders);

}
