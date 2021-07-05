package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.utils.UUIDGenerator;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import com.yyds.recipe.vo.SuccessCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public ServiceVO<?> postRecipe(Recipe recipe) {

        if (recipe.getUserId() == null || recipeMapper.getUserByUserId(recipe.getUserId()) == null) {
            return new ServiceVO<>(ErrorCode.BUSINESS_PARAMETER_ERROR, ErrorCode.BUSINESS_PARAMETER_ERROR_MESSAGE);
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
            return new ServiceVO<>(ErrorCode.DATABASE_GENERAL_ERROR, ErrorCode.DATABASE_GENERAL_ERROR_MESSAGE);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("recipeId", recipe.getRecipeId());
        return new ServiceVO<>(SuccessCode.SUCCESS_CODE, SuccessCode.SUCCESS_MESSAGE, resultMap);
    }
}
