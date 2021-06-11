package com.yyds.recipe.controller;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        User user = null;
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
    public Map<String, Object> editProfile(@RequestBody User user){
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

    @RequestMapping(value = "/editPassword", method = RequestMethod.POST)
    public Map<String, Object> editPassword(@RequestParam(value = "oldPassword") String oldPassword, @RequestParam(value = "newPassword") String newPassword, @RequestParam(value = "userId") String userId) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        try {
            userService.editPassword(oldPassword, newPassword, userId);
        } catch (Exception e) {
            rsp.put("error message", e.toString());
            rsp.put("code", -1);
            return rsp;
        }
        return rsp;
    }
}
