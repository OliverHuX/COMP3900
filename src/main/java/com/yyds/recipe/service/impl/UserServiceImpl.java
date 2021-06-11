package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.BcryptPasswordUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    @Override
    public void saveUser(User user) {

        // if user not set nick name, let nickname = firstName + " " + lastName
        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }
        // set userId
        user.setUserId(UUIDGenerator.createUserId());

        // encode password
        user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));

        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @SneakyThrows
    @Override
    // TODO: not correct, need fix
    public User loginUser(LoginUser loginUser) {

        // match password
        User user = userMapper.getUser(loginUser.getEmail());

        // password
        if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), user.getPassword())) {
            throw new Exception();
        }
        return user;

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    public void editUser(User user){
        userMapper.editUser(user);
    }
}
