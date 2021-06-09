package com.yyds.recipe.controller;

import com.yyds.recipe.service.UserService;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.yyds.recipe.model.User;

import java.util.Map;

@Controller
@Validated
public class RegisterController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> rsp = ResponseUtil.getResponse();
        try {
            userService.saveUser(user);
        } catch (Exception e) {
            rsp.put("error message", e.toString());
            rsp.put("code", -1);
            return rsp;
        }
        return rsp;
    }

}
