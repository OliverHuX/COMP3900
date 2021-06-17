package com.yyds.recipe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UserSession;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0 || cookies == null) {
            // redirect
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        String cookieInfo = null;
        for (Cookie cookie : cookies) {
            if ("user-login-cookie".equals(cookie.getName())) {
                cookieInfo = cookie.getValue();
                break;
            }
        }

        if (cookieInfo == null || cookieInfo.length() == 0) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        HttpSession session = request.getSession();
        UserSession userSession = (UserSession) session.getAttribute(UserSession.ATTRIBUTE_ID);
        if (userSession == null || userSession.getUserId() == null) {
            String userId = request.getParameter("userId");
            UserSession newSession = new UserSession(userId);
            session.setAttribute(UserSession.ATTRIBUTE_ID, newSession);
        }

        // UserSession userSession = (UserSession) request.getSession().getAttribute(UserSession.ATTRIBUTE_ID);
        // if (userSession == null || userSession.getUserId() == null) {
        //     Map<String, Object> rsp = ResponseUtil.getResponse();
        //     rsp.put("code", -1);
        //     rsp.put("error message", "invalid login");
        //     response.setContentType("application/json; charset=utf-8");
        //     ObjectMapper objectMapper = new ObjectMapper();
        //     objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //     response.getWriter().println(objectMapper.writeValueAsString(rsp));
        //     response.getWriter().close();
        //     response.flushBuffer();
        //     response.sendRedirect(request.getContextPath() + "/login");
        //     return false;
        // }

        return true;
    }
}
