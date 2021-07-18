package com.yyds.recipe.service;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface RecipeService {
    ResponseEntity<?> postRecipe(String userId, MultipartFile[] uploadPhotos, Recipe recipe);

    ResponseEntity<?> likeRecipe(String userId, Recipe recipe);

    ResponseEntity<?> unlikeRecipe(String userId, Recipe recipe);

    ResponseEntity<?> commentRecipe(String viewerUserId, String recipeId, String comment);

    ResponseEntity<?> deleteComment(String viewerUserId, String recipeId);

    ResponseEntity<?> summaryRecipe(Recipe recipe);

    ResponseEntity<?> subscribeRecipe(User viewer, Recipe recipe);

    ResponseEntity<?> cancelSubscribeRecipe(User viewer, Recipe recipe);

    ResponseEntity<?> setPrivacyRecipe(Recipe recipe, Boolean privacy);

    ResponseEntity<?> collectRecipe(String viewerUserId, String collectionId, String recipeId);
}
