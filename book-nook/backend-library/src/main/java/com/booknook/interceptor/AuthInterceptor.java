package com.booknook.interceptor;

import com.booknook.common.AuthContext;
import com.booknook.common.LoginUser;
import com.booknook.exception.BusinessException;
import com.booknook.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 登录校验拦截器。 */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final JwtUtil jwtUtil;

    public AuthInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            throw new BusinessException(401, "请先登录");
        }
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        try {
            LoginUser user = jwtUtil.parse(token);
            AuthContext.set(user);
            return true;
        } catch (Exception e) {
            throw new BusinessException(401, "登录已过期，请重新登录");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}
