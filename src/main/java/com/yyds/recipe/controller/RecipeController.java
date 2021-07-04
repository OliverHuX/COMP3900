package com.yyds.recipe.controller;

import com.yyds.recipe.service.RecipeService;
import com.yyds.recipe.vo.ServiceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/user/postRecipe", method = RequestMethod.POST)
    public ServiceVO<?> postRecipe() {
        return recipeService.postRecipe();
    }
}
