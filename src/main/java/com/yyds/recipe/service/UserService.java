package com.yyds.recipe.service;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.vo.ServiceVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface UserService {

    ServiceVO<?> register(User user, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response);

    // String saveUser(User user);

    ServiceVO<?> loginUser(LoginUser loginUser, HttpSession httpSession, HttpServletRequest request, HttpServletResponse response);

    ServiceVO<?> logoutUser(String userId, HttpSession httpSession, HttpServletResponse response);

    ServiceVO<?> editUser(User user);

    ServiceVO<?> editPassword(String newPassword, String userId);
}
