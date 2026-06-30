package com.booknook.common;

/** 当前请求登录用户上下文。 */
public class AuthContext {
    private static final ThreadLocal<LoginUser> HOLDER = new ThreadLocal<>();

    public static void set(LoginUser user) {
        HOLDER.set(user);
    }

    public static LoginUser get() {
        return HOLDER.get();
    }

    public static void clear() {
        HOLDER.remove();
    }
}
