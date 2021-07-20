package com.yyds.recipe.service;

import com.yyds.recipe.model.Follow;
import com.yyds.recipe.model.User;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    ResponseEntity<?> register(User user, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> loginUser(User loginUser, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logoutUser(String userId,HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> editUser(User user, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> editPassword(String oldPassword, String newPassword, String userId);

    ResponseEntity<?> emailVerify(String token);

    ResponseEntity<?> followUser(Follow follow);

    ResponseEntity<?> unfollowUser(Follow unfollowReq);

    ResponseEntity<?> devRegister(User user);
}
