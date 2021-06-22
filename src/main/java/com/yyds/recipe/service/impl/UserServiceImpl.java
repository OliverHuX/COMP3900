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

    private static final String PASSWORD_REGEX_PATTERN = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,30}$";
    private static final int PASSWORD_LENGTH = 6;

    @SneakyThrows
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    @Override
    public String saveUser(User user) {

        // TODO: should check why annotation does not work
        if (user.getFirstName() == null || user.getLastName() == null || user.getGender() == null
                || user.getEmail() == null || user.getPassword() == null || user.getBirthdate() == null) {
            throw new Exception("parameter is wrong");
        }

        // if user not set nick name, let nickname = firstName + " " + lastName
        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }
        // set userId
        user.setUserId(UUIDGenerator.createUserId());

        user.setCreateTime(String.valueOf(System.currentTimeMillis()));

        // encode password
        // user.setPassword(BcryptPasswordUtil.encodePassword(user.getPassword()));

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

        return user.getUserId();

    }

    @SneakyThrows
    @Override
    public LoginUser loginUser(LoginUser loginUser) {

        // match password
        LoginUser loginUserInfo = userMapper.getLoginUserInfo(loginUser.getEmail());
        // password
        // if (!BcryptPasswordUtil.passwordMatch(loginUser.getPassword(), loginUserInfo.getPassword())) {
        //     throw new Exception();
        // }
        return loginUserInfo;

    }

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    public void editUser(User user) {

        if (user.getUserId() == null) {
            throw new Exception("less userId");
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

        if (newPassword.length() < PASSWORD_LENGTH || !newPassword.matches(PASSWORD_REGEX_PATTERN)) {
            throw new Exception("password's length is less than 6 or password must have digit and word");
        }

        String userPassword = userMapper.getPasswordByUserid(userId);

        // if (!BcryptPasswordUtil.passwordMatch(oldPassword, userPassword)) {
        //     throw new Exception("old password does not match");
        // }
        //
        // String encodeNewPassword = BcryptPasswordUtil.encodePassword(newPassword);
        //
        // try {
        //     userMapper.changePassword(userId, encodeNewPassword);
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     throw e;
        // }
    }

}
