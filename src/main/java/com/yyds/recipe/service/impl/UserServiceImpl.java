package com.yyds.recipe.service.impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private int idCounter = 1;

//    @Override
//    public void login(User user, HttpSession httpSession) {
//        userMapper.RegisterUser(user);
//    }


    @Override
    public void saveUser(User user) {
        userMapper.saveUser(user);
    }

    @Override
    public boolean validEmail(String email) {
        Pattern regex = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regex.matcher(email);
        return matcher.find();
    }

    @Override
    public int register(User user) {
        if (!validEmail(user.getEmail())) {
            // do something;
            //throw new InvalidEmailException("Invalid Email Address");
            return 0;
        }

        // requirements for firstName, lastName
        // encode password;
        //user.setPassword("encode password");

        if (user.getNickName() == null) {
            user.setNickName(user.getFirstName() + " " + user.getLastName());
        }

        //String uniqueId = UUID.randomUUID().toString();

        user.setUserId(idCounter++);

        // save the user to the database
        saveUser(user);

        return idCounter;

    }

}
