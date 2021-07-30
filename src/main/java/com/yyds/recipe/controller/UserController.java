package com.yyds.recipe.controller;


import com.yyds.recipe.model.Follow;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

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
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return userService.logoutUser(request, response);
    }

    @RequestMapping(value = "/myProfile", method = RequestMethod.GET)
    public ResponseEntity<?> getMyProfile(HttpServletRequest request, HttpServletResponse response) {
        return userService.getMyPersonalProfile(request, response);
    }

    @RequestMapping(value = "/editProfile", method = RequestMethod.POST)
    public ResponseEntity<?> editProfile(@RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto, @RequestPart(value = "jsonData") User user, HttpServletRequest request, HttpServletResponse response) {
        return userService.editUser(profilePhoto, user, request, response);
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
        return userService.editPassword(req.getOldPassword(), req.getNewPassword(), request, response);
    }

    @RequestMapping(value = "/emailVerify/{token}", method = RequestMethod.GET)
    public ResponseEntity<?> emailVerify(@PathVariable String token) {
        return userService.emailVerify(token);
    }

    @RequestMapping(value = "/user/follow", method = RequestMethod.POST)
    public ResponseEntity<?> followUser(@RequestBody Follow followReq) {
        return userService.followUser(followReq);
    }

    @RequestMapping(value = "user/unfollow", method = RequestMethod.POST)
    public ResponseEntity<?> unfollowUser(@RequestBody Follow unfollowReq) {
        return userService.unfollowUser(unfollowReq);
    }


    @RequestMapping(value = "/dev/register", method = RequestMethod.POST)
    public ResponseEntity<?> devRegister(@RequestBody User user) {
        return userService.devRegister(user);
    }
}

