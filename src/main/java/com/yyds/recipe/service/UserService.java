package com.yyds.recipe.service;

import com.yyds.recipe.model.Follow;
import com.yyds.recipe.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    ResponseEntity<?> register(User user, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> loginUser(User loginUser, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> editUser(MultipartFile profilePhoto, User user, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> editPassword(String oldPassword, String newPassword, HttpServletRequest request, HttpServletResponse response);

    ResponseEntity<?> emailVerify(String token);

    ResponseEntity<?> followUser(Follow follow);

    ResponseEntity<?> unfollowUser(Follow unfollowReq);

    ResponseEntity<?> devRegister(User user);

    ResponseEntity<?> getMyPersonalProfile(HttpServletRequest request, HttpServletResponse response);
}
