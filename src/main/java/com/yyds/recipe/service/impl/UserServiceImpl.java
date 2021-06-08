package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.RegisterUser;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.BcryptPasswordUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Transactional(rollbackFor = {RuntimeException.class, Error.class, SQLException.class})
    @Override
    public void saveUser(RegisterUser registerUser) {

        // if user not set nick name, let nickname = firstName + " " + lastName
        if (registerUser.getNickName() == null) {
            registerUser.setNickName(registerUser.getFirstName() + " " + registerUser.getLastName());
        }
        // set userId
        registerUser.setUserId(UUIDGenerator.createUserId());

        // encode password
        registerUser.setPassword(BcryptPasswordUtil.encodePassword(registerUser.getPassword()));

        try {
            userMapper.saveUser(registerUser);
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

}
