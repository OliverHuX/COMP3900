package com.yyds.recipe.controller;


import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;

@RestController
// @Validated
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ServiceVO<?> register(@RequestBody User userReq, HttpServletRequest request, HttpServletResponse response) {
        return userService.register(userReq, request, response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ServiceVO<?> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.loginUser(user, request, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ServiceVO<?> logout(@RequestParam(value = "userId") String userId, HttpServletRequest request, HttpServletResponse response) {
        return userService.logoutUser(userId, request, response);
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public ServiceVO<?> editProfile(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.editUser(user, request, response);
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
    public ServiceVO<?> editPassword(@RequestBody editPasswordReq req, HttpServletRequest request, HttpServletResponse response) {
        return userService.editPassword(req.getOldPassword(), req.getNewPassword(), req.userId);
    }

    @RequestMapping(value = "/emailVerify/{token}", method = RequestMethod.GET)
    public ServiceVO<?> emailVerify(@PathVariable String token) {
        return userService.emailVerify(token);
    }

    // TODO: just for test! Delete me!
    @RequestMapping(value = "/TestIndex", method = RequestMethod.GET)
    public ServiceVO<?> testIndex() {
        ServiceVO<Object> serviceVO = new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
        String userId = "5f08f1d2-8c35-417b-9016-17c413be6a4f";
        serviceVO.setData(userId);
        return serviceVO;
    }

    @RequestMapping(value = "/TestMySql", method = RequestMethod.GET)
    public ServiceVO<?> testSql() {
        return userService.testSqlOnly();
    }

    @RequestMapping(value = "/TestRedis", method = RequestMethod.GET)
    public ServiceVO<?> testRedis() {
        return userService.testRedisOnly();
    }

}

