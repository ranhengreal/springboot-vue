package com.hezheng.authapi.common;

import com.hezheng.authapi.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JWTInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 跨域预检请求直接放行
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }

        // 获取token
        String token = request.getHeader("token");
        if (token == null){
            token = request.getParameter("token");
        }
        if (token == null || token.isEmpty()) {
            throw new CustomException(401, "您无权操作！");
        }

        // 验证token
        if (!jwtUtil.validateToken(token)) {
            throw new CustomException(401, "您无权操作！");
        }

        return true;
    }
}
