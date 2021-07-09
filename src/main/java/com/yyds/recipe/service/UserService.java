package com.yyds.recipe.service;

import com.yyds.recipe.model.User;
import com.yyds.recipe.vo.ServiceVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface UserService {

    ServiceVO<?> register(User user, HttpServletRequest request, HttpServletResponse response);

    ServiceVO<?> loginUser(User loginUser, HttpServletRequest request, HttpServletResponse response);

    ServiceVO<?> logoutUser(String userId,HttpServletRequest request, HttpServletResponse response);

    ServiceVO<?> editUser(User user, HttpServletRequest request, HttpServletResponse response);

    ServiceVO<?> editPassword(String oldPassword, String newPassword, String userId);

    ServiceVO<?> emailVerify(String token);

    ServiceVO<?> testSqlOnly();

    ServiceVO<?> testRedisOnly();
}
