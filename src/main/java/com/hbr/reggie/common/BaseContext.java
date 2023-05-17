package com.hbr.reggie.common;

/**
 * @ClassName BaseContext
 * @Description TODO
 * @Author Hbr
 * @Date 2023/5/9 17:19
 * @Version 1.0
 */
public class BaseContext {
    private static final ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setUserId(Long userId) {
        threadLocal.set(userId);
    }

    public static Long getUserId() {
        return threadLocal.get();
    }
}
