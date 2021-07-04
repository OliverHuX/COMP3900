package com.yyds.recipe.service.impl;

import com.yyds.recipe.mapper.RecipeMapper;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.vo.ServiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeMapper recipeMapper;

    @Override
    public ServiceVO<?> postRecipe() {
        return null;
    }
}
