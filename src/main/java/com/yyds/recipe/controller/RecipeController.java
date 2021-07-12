package com.yyds.recipe.controller;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.vo.ServiceVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/user/postRecipe", method = RequestMethod.POST)
    public ServiceVO<?> postRecipe(Recipe recipe) {
        return recipeService.postRecipe(recipe);
    }

    @RequestMapping(value = "/recipe/like", method = RequestMethod.POST)
    public ServiceVO<?> likeRecipe(@RequestBody Recipe recipe) {
        return recipeService.likeRecipe(recipe);
    }

    @RequestMapping(value = "/recipe/unlike", method = RequestMethod.POST)
    public ServiceVO<?> unlikeRecipe(@RequestBody Recipe recipe) {
        return recipeService.unlikeRecipe(recipe);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class commentRecipeReq {
        @NotNull
        private User viewer;
        @NotNull
        private Recipe recipe;
        @NotNull
        private String comment;
    }

    @RequestMapping(value = "/recipe/comment", method = RequestMethod.POST)
    public ServiceVO<?> commentRecipe(@RequestBody commentRecipeReq req) {
        return recipeService.commentRecipe(req.getViewer(), req.getRecipe(), req.getComment());
    }
}
