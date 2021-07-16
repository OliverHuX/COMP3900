package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.CollectionMapper;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.CollectionService;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    private Helper helper = new Helper();

    @Override
    public ResponseEntity<?> addCollection(String userId, String collectionName) {

        ResponseEntity<?> userError = helper.verifyUserExist(userId);

        if (userError != null) {
            return userError;
        }

        if (collectionName == null) {
            return ResponseUtil.getResponse(ResponseCode.BUSINESS_PARAMETER_ERROR, null, null);
        }

        User user = userMapper.getUserByUserId(userId);

        String collectionId = UUIDGenerator.createCollectionId();
        Collection newCollection = new Collection();
        newCollection.setCollectionId(collectionId);
        newCollection.setCollectionName(collectionName);
        newCollection.setCollectorId(userId);

        user.addCollection(newCollection);

        try {
            userMapper.updateCollections(userId, user.getCollections());
            collectionMapper.saveCollection(newCollection);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> removeCollection(String userId, String collectionId) {

        ResponseEntity<?> userError = helper.verifyUserExist(userId);

        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(collectionId);

        if (collectionError != null) {
            return collectionError;
        }

        User user = userMapper.getUserByUserId(userId);
        user.removeCollection(collectionId);

        try {
            userMapper.updateCollections(userId, user.getCollections());
            collectionMapper.removeCollection(collectionId);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);

    }

    @Override
    public ResponseEntity<?> changeCollectionName(String userId, String collectionId, String collectionName) {

        ResponseEntity<?> userError = helper.verifyUserExist(userId);

        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(collectionId);

        if (collectionError != null) {
            return collectionError;
        }

        User user = userMapper.getUserByUserId(userId);

        HashMap<String, Collection> collections = user.getCollections();
        collections.get(collectionId).setCollectionName(collectionName);

        try {
            userMapper.updateCollections(userId, collections);
            collectionMapper.changeCollectionName(collectionId, collectionName);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> addRecipeToCollection(String userId, String collectionId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(userId);

        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(collectionId);

        if (collectionError != null) {
            return collectionError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);

        if (recipeError != null) {
            return recipeError;
        }

        User user = userMapper.getUserByUserId(userId);
        Recipe recipe = recipeMapper.getRecipeById(recipeId);

        HashMap<String, Collection> collections = user.getCollections();
        collections.get(collectionId).addRecipe(recipe);

        try {
            userMapper.updateCollections(userId, collections);
            collectionMapper.updateCollectionRecipes(collectionId, collections.get(collectionId).getRecipes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);

    }

    @Override
    public ResponseEntity<?> removeRecipeFromCollection(String userId, String collectionId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(userId);

        if (userError != null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(collectionId);

        if (collectionError != null) {
            return collectionError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);

        if (recipeError != null) {
            return recipeError;
        }

        User user = userMapper.getUserByUserId(userId);
        Recipe recipe = recipeMapper.getRecipeById(recipeId);

        HashMap<String, Collection> collections = user.getCollections();
        collections.get(collectionId).removeRecipe(recipe);

        try {
            userMapper.updateCollections(userId, collections);
            collectionMapper.updateCollectionRecipes(collectionId, collections.get(collectionId).getRecipes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

}
