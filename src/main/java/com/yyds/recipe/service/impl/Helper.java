package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.CollectionMapper;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class Helper {

    @Autowired
    private RecipeMapper recipeMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CollectionMapper collectionMapper;

    public ResponseEntity<?> verifyRecipeExist(String recipeId) {

        if (recipeId == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        try {
            recipeMapper.getRecipeById(recipeId);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        return null;
    }

    public  ResponseEntity<?> verifyUserExist(String userId) {

        if (userId == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        try {
            userMapper.getUserByUserId(userId);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        return null;
    }

    public  ResponseEntity<?> verifyCollectionExist(String collectionId) {

        if (collectionId == null) {
            return ResponseUtil.getResponse(ResponseCode.COLLECTION_ID_NOT_FOUND, null, null);
        }

        try {
            collectionMapper.getCollectionById(collectionId);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.COLLECTION_ID_NOT_FOUND, null, null);
        }

        return null;
    }

}
