package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.AuthorizationException;
import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.FollowMapper;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.FollowService;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl implements FollowService {

    @Autowired
    private UserMapper userMapper;


    @Autowired
    private FollowMapper followMapper;

    @Override
    public ResponseEntity<?> followUser(String userId, String followId) {
        User checkedUser = userMapper.getUserByUserId(userId);
        if (checkedUser == null) {
            throw new AuthorizationException();
        }

        User checkedFollow = userMapper.getUserByUserId(followId);
        if (checkedFollow == null) {
            return ResponseUtil.getResponse(ResponseCode.FOLLOW_USER_NOT_EXIST, null, null);
        }

        try {
            followMapper.followUser(userId, followId);
        } catch (Exception e) {
            throw new AuthorizationException();
        }
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }
}
