package com.sme.utils;



/**
 * 用户上下文类
 * 实现用户信息的线程安全存储与访问
 */
public class UserContext {

    //存储当前线程的用户ID，线程安全
    private static final ThreadLocal<Long> userIdHolder = new ThreadLocal<>();

    //存储当前线程的用户名，线程安全
    private static final ThreadLocal<String> usernameHolder = new ThreadLocal<>();

    /**
     * 设置当前线程的用户ID
     * @param userId 用户ID
     */
    public static void setUserId(Long userId) {
        userIdHolder.set(userId);
    }

    /**
     * 获取当前线程的用户ID
     * @return 用户ID
     */
    public static Long getUserId() {
        return userIdHolder.get();
    }

    /**
     * 设置当前线程的用户名
     * @param username 用户名
     */
    public static void setUsername(String username) {
        usernameHolder.set(username);
    }
    /**
     * 获取当前线程的用户名
     * @return 用户名
     */
    public static String getUsername() {
        return usernameHolder.get();
    }

    /**
     * 清除当前线程的用户信息
     */
    public static void clear() {
        userIdHolder.remove();
        usernameHolder.remove();
    }

}
