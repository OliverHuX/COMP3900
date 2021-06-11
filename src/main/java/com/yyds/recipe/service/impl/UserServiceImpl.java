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


    // @Override
    // public boolean validEmail(String email) {
    //     Pattern regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    //     Matcher matcher = regex.matcher(email);
    //     return matcher.find();
    // }

    // @Override
    // public int register(RegisterUser registerUser) {
    //     if (!validEmail(registerUser.getEmail())) {
    //         // do something;
    //         //throw new InvalidEmailException("Invalid Email Address");
    //         return 0;
    //     }
    //
    //     // requirements for firstName, lastName
    //     // encode password;
    //     //user.setPassword("encode password");
    //
    //     if (registerUser.getNickName() == null) {
    //         registerUser.setNickName(registerUser.getFirstName() + " " + registerUser.getLastName());
    //     }
    //
    //     //String uniqueId = UUID.randomUUID().toString();
    //
    //     registerUser.setUserId(idCounter++);
    //
    //     // save the user to the database
    //     saveUser(registerUser);
    //
    //     return idCounter;
    //
    // }

    @SneakyThrows
    @Override
    public void editPassword(String oldPassword, String newPassword, String userId) {

        User user = userMapper.getUserbyId(userId);

        if (newPassword.length() < 6 || ! newPassword.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
            throw new Exception();
        }

        if (!BcryptPasswordUtil.passwordMatch(oldPassword, user.getPassword())) {
            throw new Exception();
        }

        user.setPassword(BcryptPasswordUtil.encodePassword(newPassword));

        try {
            userMapper.saveUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
