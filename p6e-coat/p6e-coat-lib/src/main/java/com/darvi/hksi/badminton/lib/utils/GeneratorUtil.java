package com.darvi.hksi.badminton.lib.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 生成序号的帮助类
 * @author lidashuang
 * @version 1.0
 */
public final class GeneratorUtil {

    /** 随机数最小长度 */
    private static final int MIN_RANDOM_LENGTH = 1;
    /** 随机数最大长度 */
    private static final int MAX_RANDOM_LENGTH = 19;
    /** 线程安全的随机数生成器对象 */
    private static final ThreadLocalRandom THREAD_LOCAL_RANDOM = ThreadLocalRandom.current();

    /**
     * 生成 UUID 数据
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 生成 6 位随机数
     * @return 6 位随机数
     */
    public static String random() {
        return random(6);
    }

    /**
     * 生产指定长度的随机数
     * @param len 长度
     * @return 长度的随机数
     */
    public static String random(int len) {
        if (len < MIN_RANDOM_LENGTH || len > MAX_RANDOM_LENGTH) {
            return "";
        } else {
            long l = 10;
            final StringBuilder r = new StringBuilder();
            for (int i = 0; i < len; i++) {
                l *= 10;
                r.append("0");
            }
            r.append(THREAD_LOCAL_RANDOM.nextLong(0, l));
            return r.substring(r.length() - len);
        }
    }

}
