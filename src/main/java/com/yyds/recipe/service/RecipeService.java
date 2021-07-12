package com.yyds.recipe.service;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.vo.ServiceVO;

public interface RecipeService {
    ServiceVO<?> postRecipe(Recipe recipe);

    ServiceVO<?> likeRecipe(Recipe recipe);

    ServiceVO<?> unlikeRecipe(Recipe recipe);

    ServiceVO<?> commentRecipe(User viewer, Recipe recipe, String comment);
}
