package com.yyds.recipe.controller;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UserSession;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServiceVO<?> register(@RequestBody User userReq, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
        return userService.register(userReq, httpSession, request, response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody LoginUser loginUser, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        LoginUser user = null;
        try {
            user = userService.loginUser(loginUser);

            UserSession userSession = new UserSession(user.getUserId());
            httpSession.setAttribute(UserSession.ATTRIBUTE_ID, userSession);

            Cookie userCookie = new Cookie("user-login-cookie", user.getUserId());
            userCookie.setMaxAge(2 * 60 * 60);
            userCookie.setPath(request.getContextPath());
            response.addCookie(userCookie);
            rsp.put("userId", user.getUserId());

        } catch (Exception e) {
            e.printStackTrace();
            rsp.put("code", -1);
            rsp.put("error message", e.toString());
            return rsp;
        }
        return rsp;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Map<String, Object> logout(@RequestParam(value = "userId") String userId, HttpSession httpSession, HttpServletResponse response ) {
        // TODO: unhandled exceptions
        Map<String, Object> rsp = ResponseUtil.getResponse();
        httpSession.removeAttribute(UserSession.ATTRIBUTE_ID);
        Cookie cookie = new Cookie("user-login-cookie", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return rsp;
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public Map<String, Object> editProfile(@RequestBody User user) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        try {
            userService.editUser(user);
        } catch (Exception e) {
            rsp.put("code", -1);
            rsp.put("error message", e.toString());
            return rsp;
        }
        return rsp;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class editPasswordReq {
        @NotNull
        private String oldPassword;
        @NotNull
        private String newPassword;
        @NotNull
        private String userId;
    }

    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    public Map<String, Object> editPassword(@RequestBody editPasswordReq req) {
        Map<String, Object> rsp = ResponseUtil.getResponse();

        String oldPassword = req.getOldPassword();
        String newPassword = req.getNewPassword();
        String userId = req.getUserId();

        try {
            userService.editPassword(oldPassword, newPassword, userId);
        } catch (Exception e) {
            rsp.put("error message", e.toString());
            rsp.put("code", -1);
            return rsp;
        }
        return rsp;
    }

    // TODO: just for test! Delete me!
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public ServiceVO<?> testIndex() {
        ServiceVO<Object> serviceVO = new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
        String userId = "5f08f1d2-8c35-417b-9016-17c413be6a4f";
        serviceVO.setData(userId);
        return serviceVO;
    }
}

