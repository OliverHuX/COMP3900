package com.yyds.recipe.service;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void login(User user, HttpSession httpSession) {
        userMapper.RegisterUser(user);
    }
}
