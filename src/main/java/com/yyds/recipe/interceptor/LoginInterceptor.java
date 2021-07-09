package com.yyds.recipe.interceptor;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.User;
import com.yyds.recipe.utils.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;


public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate<String, Serializable> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // String requestURI = request.getRequestURI();
        // if (requestURI.contains("/login") || requestURI.contains("/register") || requestURI.contains("emailVerify")) {
        //     return true;
        // }
        String token = request.getHeader("token");
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        if (StringUtils.isEmpty(token)) {
            throw new Exception();
        }

        String userId = JwtUtil.decodeToken(token).getClaim("userId").asString();
        User user = userMapper.getUserByUserId(userId);
        if (user == null) {
            throw new Exception();
        }
        if (!redisTemplate.hasKey(token)) {
            throw new Exception();
        }
        return true;
    }
}
