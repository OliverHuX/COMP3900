package com.yyds.recipe.service;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;

public interface UserService {

    void saveUser(User user);

    User loginUser(LoginUser loginUser);

}
