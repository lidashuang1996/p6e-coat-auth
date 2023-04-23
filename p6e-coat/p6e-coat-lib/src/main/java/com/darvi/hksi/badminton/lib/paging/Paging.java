package com.darvi.hksi.badminton.lib.paging;

/**
 * 分页的配置接口
 * @author lidashuang
 * @version 1.0
 */
public interface Paging {

    /**
     * 格式化分页的页码
     * @param page 分页页码
     * @return 格式化之后的分页页码
     */
    int formatPage(Integer page);

    /**
     * 格式化分页的长度
     * @param size 分页长度
     * @return 格式化之后的分页长度
     */
    int formatSize(Integer size);

}
