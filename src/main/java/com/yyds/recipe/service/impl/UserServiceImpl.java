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

    private static final String PASSWORD_REGEX_PATTERN = "^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$";
    private static final int PASSWORD_LENGTH = 6;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    @Override
    public void saveUser(User user) {

        // if user not set nick name, let nickname = firstName + " " + lastName
        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }
        // set userId
        user.setUserId(UUIDGenerator.createUserId());

        user.setCreateTime(String.valueOf(System.currentTimeMillis()));

        // encode password
        user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));

        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        try {
            userMapper.saveUserAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @SneakyThrows
    @Override
    public LoginUser loginUser(LoginUser loginUser) {

        // match password
        LoginUser loginUserInfo = userMapper.getLoginUserInfo(loginUser.getEmail());
        // password
        if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), loginUserInfo.getPassword())) {
            throw new Exception();
        }
        return loginUserInfo;

    }

    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    public void editUser(User user){

        // if user not set nick name, let nickname = firstName + " " + lastName
        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }

        // if user not set gender, let gender = other
        if (user.getGender() < 0 || user.getGender() > 2) {
            user.setGender(2);
        }

        try {
            userMapper.editUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @SneakyThrows
    @Override
    public void editPassword(String oldPassword, String newPassword, String userId) {

        if (newPassword.length() < PASSWORD_LENGTH || ! newPassword.matches(PASSWORD_REGEX_PATTERN)) {
            throw new Exception();
        }

        String userPassword = userMapper.getPasswordByUserid(userId);

        if (!BcryptPasswordUtil.passwordMatch(oldPassword, userPassword)) {
            throw new Exception();
        }

        String encodeNewPassword = BcryptPasswordUtil.encodePassword(newPassword);

        try {
            userMapper.changePassword(userId, encodeNewPassword);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
