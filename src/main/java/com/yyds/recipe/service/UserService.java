package com.yyds.recipe.service;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;

public interface UserService {

    String saveUser(User user);

    LoginUser loginUser(LoginUser loginUser);

    void editUser(User user);

    void editPassword(String oldPassword, String newPassword, String userId);
}
