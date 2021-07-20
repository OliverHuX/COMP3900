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

    ResponseEntity<?> getAllPublicRecipes(int pageNum, int pageSize);

    ResponseEntity<?> commentRecipe(String viewerUserId, String recipeId, String comment);

    ResponseEntity<?> deleteComment(String viewerUserId, String recipeId);

    ResponseEntity<?> summaryRecipe(Recipe recipe);

    ResponseEntity<?> subscribeRecipe(User viewer, Recipe recipe);

    ResponseEntity<?> cancelSubscribeRecipe(User viewer, Recipe recipe);

    ResponseEntity<?> setPrivacyRecipe(Recipe recipe);

    ResponseEntity<?> collectRecipe(String viewerUserId, String collectionId, String recipeId);

    ResponseEntity<?> getMyRecipes(int pageNum, int pageSize, HttpServletRequest request);
}
