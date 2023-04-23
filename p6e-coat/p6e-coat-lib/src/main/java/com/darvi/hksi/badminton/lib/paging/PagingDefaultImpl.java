package com.darvi.hksi.badminton.lib.paging;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

/**
 * 分页的配置接口默认实现
 * @author lidashuang
 * @version 1.0
 */
@Component(PagingDefaultImpl.BEAN_NAME)
@ConditionalOnMissingBean(
        value = Paging.class,
        ignored = PagingDefaultImpl.class
)
public class PagingDefaultImpl implements Paging {

    /**
     * 注入的 BEAN 的名称
     */
    public static final String BEAN_NAME = "com.darvi.hksi.badminton.lib.paging.PagingDefaultImpl";

    /** 查询数据的最大长度 */
    private final static int MAX_SIZE = 200;

    @Override
    public int formatPage(Integer page) {
        return (page == null || page < 1) ? 1 : page;
    }

    @Override
    public int formatSize(Integer size) {
        return (size == null || size < 1) ? 16 : (size > MAX_SIZE ? MAX_SIZE : size);
    }

}
