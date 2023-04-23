package com.darvi.hksi.badminton.lib.context;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 基础的上下文对象
 *
 * @author lidashuang
 * @version 1.0
 */
public class BaseContext implements Serializable {

    @Data
    @Accessors(chain = true)
    public static class PagingParam implements Serializable {
        private Integer size;
        private Integer page;
    }

    @Data
    @Accessors(chain = true)
    public static class OrderParam implements Serializable {
        /**
         * 排序的 ASC
         */
        private static final String ASC = "ASC";

        /**
         * 排序的 DESC
         */
        private static final String DESC = "DESC";

        /**
         * 排序的分割符号
         */
        private static final String DIVISION_CHAR = " ";

        private List<String> orders;

        /**
         * 验证 ORDER 合法性
         *
         * @param rules 规则合法字段
         * @return 验证的结果
         */
        public boolean verificationOrders(List<String> rules) {
            if (rules == null) {
                return false;
            }
            if (this.orders != null && this.orders.size() != 0) {
                for (final String o : this.orders) {
                    boolean status = false;
                    final String[] os = o.split(DIVISION_CHAR);
                    if (os.length == 2) {
                        final String name = os[0];
                        for (final String rule : rules) {
                            if (rule.equalsIgnoreCase(name)) {
                                status = true;
                                break;
                            }
                        }
                    }
                    if (!status) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * 按照顺序转换为数组输出
         *
         * @return 排序的数组内容
         */
        public String[] toArrayOrders() {
            if (this.orders == null || this.orders.size() == 0) {
                return new String[0];
            } else {
                final String[] result = new String[this.orders.size()];
                for (int i = 0; i < this.orders.size(); i++) {
                    final String o = this.orders.get(i);
                    final String[] os = o.split(DIVISION_CHAR);
                    final String name = os[0];
                    final String condition = os.length > 1 ? (os[1].equalsIgnoreCase(DESC) ? DESC : ASC) : ASC;
                    result[i] = name + DIVISION_CHAR + condition;
                }
                return result;
            }
        }
    }

    @Data
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class PagingAndOrderParam extends OrderParam implements Serializable {
        private Integer size;
        private Integer page;
    }

    @Data
    @Accessors(chain = true)
    public static class ListResult implements Serializable {
        private Integer size;
        private Integer page;
        private Long total;
    }
}
