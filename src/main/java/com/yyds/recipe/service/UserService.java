package com.yyds.recipe.service;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yyds.recipe.model.User;

import javax.servlet.http.HttpSession;

public interface UserService {

//    void login(User user, HttpSession httpSession);

    void saveUser(User user);

    boolean validEmail (String email);

    int register(User user);
}
