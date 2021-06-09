package com.yyds.recipe.controller;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

@Controller
@Validated
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Map<String, Object> login(@RequestBody LoginUser loginUser) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        User user = null;
        try {
            user = userService.loginUser(loginUser);
        } catch (Exception e) {
            e.printStackTrace();
            rsp.put("code", -1);
            rsp.put("error message", e.toString());
            return rsp;
        }
        rsp.put("userId", user.getUserId());
        return rsp;
    }
}
