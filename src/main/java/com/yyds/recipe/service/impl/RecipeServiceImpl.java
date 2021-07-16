package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.mapper.UserMapper;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecipeMapper recipeMapper;

    private Helper helper = new Helper();

    @Override
    public ResponseEntity<?> postRecipe(Recipe recipe) {

        if (recipe.getUserId() == null || userMapper.getUserByUserId(recipe.getUserId()) == null) {
            // return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
            return ResponseUtil.getResponse(ResponseCode.ERROR, null, null);
        }

        // TODO: not sure
        // if (recipe.getImage() == null) {
        //     return new ServiceVO<>(ErrorCode.IMAGE_VERIFY_ERROR, ErrorCode.IMAGE_VERIFY_ERROR_MESSAGE);
        // }

        String recipeId = UUIDGenerator.createRecipeId();
        recipe.setRecipeId(recipeId);
        recipe.setCreateTime(String.valueOf(System.currentTimeMillis()));
        recipe.setLikes(0);

        if (recipe.getIntroduction() == null) {
            recipe.setIntroduction("The guy is very lazy");
        }

        try {
            recipeMapper.saveRecipe(recipe);
        } catch (Exception e) {
            // return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null
            //);
            return ResponseUtil.getResponse(ResponseCode.ERROR, null, null);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());
        // return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> likeRecipe(String recipeId) {

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError!= null) {
            return recipeError;
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.setLikes(recipe.getLikes() + 1);

        try {
            recipeMapper.updateRecipeLikes(recipeId, recipe.getLikes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> unlikeRecipe(String recipeId) {

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError!= null) {
            return recipeError;
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.setLikes(recipe.getLikes() - 1);
        try {
            recipeMapper.updateRecipeLikes(recipeId, recipe.getLikes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> commentRecipe(String viewerUserId, String recipeId, String comment) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError!= null) {
            return userError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError!= null) {
            return recipeError;
        }

        if (comment == null) {
            return ResponseUtil.getResponse(ResponseCode.BUSINESS_PARAMETER_ERROR, null, null);
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.addComment(viewerUserId, comment);

        try {
            recipeMapper.updateRecipeComments(recipeId, recipe.getComments());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> deleteComment(String viewerUserId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError!= null) {
            return userError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError!= null) {
            return recipeError;
        }

        Recipe recipe = recipeMapper.getRecipeById(recipeId);
        recipe.deleteComment(viewerUserId);

        try {
            recipeMapper.updateRecipeComments(recipeId, recipe.getComments());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> summaryRecipe(Recipe recipe) {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (Objects.nonNull(recipeError)) {
            resultMap.put("recipeError", recipeError);
            return ResponseUtil.getResponse(ResponseCode.ERROR, null, resultMap);
        }

        resultMap.put("summary", recipe.getIntroduction());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ResponseEntity<?> subscribeRecipe(User viewer, Recipe recipe) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (recipeError!= null) {
            return recipeError;
        }

        try {
            viewer.getSubscribes().add(recipe.getRecipeId());
            recipeMapper.updateSubscribe(viewer.getUserId(), viewer.getSubscribes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> cancelSubscribeRecipe(User viewer, Recipe recipe) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (recipeError!= null) {
            return recipeError;
        }

        try {
            viewer.getSubscribes().remove(recipe.getRecipeId());
            recipeMapper.deleteSubscribe(viewer.getUserId(), viewer.getSubscribes());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> setPrivacyRecipe(Recipe recipe, Boolean privacy) {

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipe.getRecipeId());
        if (recipeError!= null) {
            return recipeError;
        }

        if (recipeMapper.getRecipeById(recipe.getRecipeId()).getIsPrivacy().equals(privacy)) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        try {
            recipeMapper.updatePrivacy(recipe.getRecipeId(), privacy);
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }

    @Override
    public ResponseEntity<?> collectRecipe(String viewerUserId, String collectionId, String recipeId) {

        ResponseEntity<?> userError = helper.verifyUserExist(viewerUserId);
        if (userError!= null) {
            return userError;
        }

        ResponseEntity<?> collectionError = helper.verifyCollectionExist(viewerUserId, );
        if (userError!= null) {
            return userError;
        }

        ResponseEntity<?> recipeError = helper.verifyRecipeExist(recipeId);
        if (recipeError!= null) {
            return recipeError;
        }




        User viewer = userMapper.getUserByUserId(viewerUserId);

        viewer.

    }
}
