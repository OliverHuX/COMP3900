package com.yyds.recipe.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Map;

@RestController
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            rsp.put("error message", e.toString());
            rsp.put("code", -1);
            return rsp;
        }
        return rsp;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody LoginUser loginUser) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        LoginUser user = null;
        try {
            user = userService.loginUser(loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            rsp.put("code", -1);
            rsp.put("error message", e.toString());
            return rsp;
        }
        rsp.put("userId", user.getUserId());
        return rsp;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public Map<String, Object> logout(@RequestParam(value = "userId") String userId) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
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

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Map<String, Object> testIndex() {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        rsp.put("text", "test message");
        return rsp;
    }
}

