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
            // return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
            return ResponseUtil.getResponse(ResponseCode.ERROR, null, null);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());
        // return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ServiceVO<?> likeRecipe(Recipe recipe) {

        ServiceVO<?> error = verifyRecipe(recipe);
        if (error!= null) {
            return error;
        }

        recipe.setLikes(recipe.getLikes() + 1);
        try {
            recipeMapper.updateRecipeLikes(recipe.getRecipeId(), recipe.getLikes());
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    public ServiceVO<?> unlikeRecipe(Recipe recipe) {

        ServiceVO<?> error = verifyRecipe(recipe);
        if (error!= null) {
            return error;
        }

        recipe.setLikes(recipe.getLikes() - 1);
        try {
            recipeMapper.updateRecipeLikes(recipe.getRecipeId(), recipe.getLikes());
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    public ServiceVO<?> commentRecipe(User viewer, Recipe recipe, String comment) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            return new ServiceVO<>(ErrorCode.USERID_NOT_FOUND_ERROR, ErrorCode.USERID_NOT_FOUND_ERROR_MESSAGE);
        }

        ServiceVO<?> error = verifyRecipe(recipe);
        if (error!= null) {
            return error;
        }

        if (comment == null) {
            return new ServiceVO<>(ErrorCode.EMPTY_COMMENT_ERROR, ErrorCode.EMPTY_COMMENT_MESSAGE);
        }

        HashMap<User, String> comments = recipe.getComments();
        comments.put(viewer, comment);

        recipe.setComments(comments);

        try {
            recipeMapper.updateRecipeComments(recipe.getRecipeId(), recipe.getComments());
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    public ServiceVO<?> verifyRecipe(Recipe recipe) {

        if (userMapper.getUserByUserId(recipe.getUserId()) == null) {
            return new ServiceVO<>(ErrorCode.USERID_NOT_FOUND_ERROR, ErrorCode.USERID_NOT_FOUND_ERROR_MESSAGE);
        }

        if (recipeMapper.getRecipeById(recipe.getRecipeId()) == null) {
            return new ServiceVO<>(ErrorCode.RECIPEID_NOT_FOUND_ERROR, ErrorCode.RECIPEID_NOT_FOUND_MESSAGE);
        }

        return null;
    }

    @Override
    public ResponseEntity<?> summaryRecipe(Recipe recipe) {

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());

        ServiceVO<?> error = verifyRecipe(recipe);
        if (Objects.nonNull(error)) {
            resultMap.put("error", error);
            return ResponseUtil.getResponse(ResponseCode.ERROR, null, resultMap);
        }

        resultMap.put("summary", recipe.getIntroduction());
        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, resultMap);
    }

    @Override
    public ServiceVO<?> subscribeRecipe(User viewer, Recipe recipe) {

        if (userMapper.getUserByUserId(viewer.getUserId()) == null) {
            return new ServiceVO<>(ErrorCode.USERID_NOT_FOUND_ERROR, ErrorCode.USERID_NOT_FOUND_ERROR_MESSAGE);
        }

        ServiceVO<?> error = verifyRecipe(recipe);
        if (error!= null) {
            return error;
        }

        try {
            viewer.getSubscribes().add(recipe.getRecipeId());
            recipeMapper.updateSubscribe(viewer.getUserId(), viewer.getSubscribes());
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }

    @Override
    public ServiceVO<?> setPrivacyRecipe(Recipe recipe, Boolean privacy) {

        ServiceVO<?> error = verifyRecipe(recipe);
        if (error!= null) {
            return error;
        }

        if (recipeMapper.getRecipeById(recipe.getRecipeId()).getIsPrivacy().equals(privacy)) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        try {
            recipeMapper.updatePrivacy(recipe.getRecipeId(), privacy);
        } catch (Exception e) {
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE);
    }
}
