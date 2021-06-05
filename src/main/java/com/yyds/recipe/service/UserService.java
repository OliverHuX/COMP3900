package com.yyds.recipe.service;

import com.yyds.recipe.model.User;

import javax.servlet.http.HttpSession;

public interface UserService {
    void login(User user, HttpSession httpSession);
}
