package com.yyds.recipe.service;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface RecipeService {
    ResponseEntity<?> postRecipe(HttpServletRequest request, MultipartFile[] uploadPhotos, Recipe recipe);

    ResponseEntity<?> likeRecipe(HttpServletRequest request, Recipe recipe);

    ResponseEntity<?> unlikeRecipe(HttpServletRequest request, Recipe recipe);

    ResponseEntity<?> getAllPublicRecipes(String recipeId, String creatorId, String search, String tags, Integer pageNum, Integer pageSize, HttpServletRequest request);

    // TODO:
    ResponseEntity<?> commentRecipe(String viewerUserId, String recipeId, String comment);

    // TODO:
    ResponseEntity<?> deleteComment(String viewerUserId, String recipeId);

    // TODO:
    ResponseEntity<?> summaryRecipe(Recipe recipe);

    // TODO:
    ResponseEntity<?> subscribeRecipe(User viewer, Recipe recipe);


    // TODO:
    ResponseEntity<?> cancelSubscribeRecipe(User viewer, Recipe recipe);

    ResponseEntity<?> setPrivacyRecipe(HttpServletRequest request, Recipe recipe);

    // TODO:
    ResponseEntity<?> collectRecipe(String viewerUserId, String collectionId, String recipeId);


    // TODO:
    ResponseEntity<?> getMyRecipes(int pageNum, int pageSize, HttpServletRequest request);

    ResponseEntity<?> testPost(HttpServletRequest request, MultipartFile[] uploadPhotos, Recipe recipe);

    ResponseEntity<?> rateRecipe(Recipe recipe, HttpServletRequest request);
}
