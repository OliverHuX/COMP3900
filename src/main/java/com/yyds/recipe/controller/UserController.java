package com.yyds.recipe.controller;


import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> register(@RequestBody User userReq, HttpServletRequest request, HttpServletResponse response) {
        return userService.register(userReq, request, response);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.loginUser(user, request, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestParam(value = "userId") String userId, HttpServletRequest request, HttpServletResponse response) {
        return userService.logoutUser(userId, request, response);
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public ResponseEntity<?> editProfile(@RequestBody User user, HttpServletRequest request, HttpServletResponse response) {
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
    public ResponseEntity<?> editPassword(@RequestBody editPasswordReq req, HttpServletRequest request, HttpServletResponse response) {
        return userService.editPassword(req.getOldPassword(), req.getNewPassword(), req.userId);
    }

    @RequestMapping(value = "/emailVerify/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> emailVerify(@PathVariable String token) {
        return userService.emailVerify(token);
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public ResponseEntity<?> testEverything() {
        return ResponseEntity.ok("Hello world!");
    }
}

