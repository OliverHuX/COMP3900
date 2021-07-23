package com.yyds.recipe.controller;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.service.RecipeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/recipe/postRecipe", method = RequestMethod.POST)
    public ResponseEntity<?> postRecipe(HttpServletRequest request,
                                        @RequestPart(value = "uploadPhotos") MultipartFile[] uploadPhotos,
                                        @RequestPart(value = "jsonData") Recipe recipe) {
        return recipeService.postRecipe(request, uploadPhotos, recipe);
    }

    @RequestMapping(value = "/recipe/like", method = RequestMethod.POST)
    public ResponseEntity<?> likeRecipe(HttpServletRequest request, @RequestBody Recipe recipe) {
        return recipeService.likeRecipe(request, recipe);
    }

    @RequestMapping(value = "/recipe/unlike", method = RequestMethod.POST)
    public ResponseEntity<?> unlikeRecipe(HttpServletRequest request, @RequestBody Recipe recipe) {
        return recipeService.unlikeRecipe(request, recipe);
    }

    @RequestMapping(value = "/recipe/set_privacy", method = RequestMethod.POST)
    public ResponseEntity<?> setRecipePrivacy(HttpServletRequest request, @RequestBody Recipe recipe) {
        return recipeService.setPrivacyRecipe(request, recipe);
    }

    @RequestMapping(value = "/recipe/recipe_list")
    public ResponseEntity<?> getRecipeList(@RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize) {
        return recipeService.getAllPublicRecipes(pageNum, pageSize);
    }

    @RequestMapping(value = "/recipe/recipe_all_list")
    public ResponseEntity<?> getAllRecipeList(@RequestParam(value = "userId", required = false) String userId, @RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize) {
        return recipeService.getAllRecipes(userId, pageNum, pageSize);
    }

    @RequestMapping(value = "/recipe/my_recipe", method = RequestMethod.GET)
    public ResponseEntity<?> getMyRecipeList(@RequestParam(value = "pageNum", required = false) int pageNum, @RequestParam(value = "pageSize", required = false) int pageSize, HttpServletRequest request) {
        return recipeService.getMyRecipes(pageNum, pageSize, request);
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class commentRecipeReq {
        @NotNull
        private String viewerUserId;
        @NotNull
        private String recipeId;
        @NotNull
        private String comment;
    }

    @RequestMapping(value = "/recipe/comment", method = RequestMethod.POST)
    public ResponseEntity<?> commentRecipe(@RequestBody commentRecipeReq req) {
        return recipeService.commentRecipe(req.getViewerUserId(), req.getRecipeId(), req.getComment());
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class deleteCommentReq {
        @NotNull
        private String viewerUserId;
        @NotNull
        private String recipeId;
    }

    @RequestMapping(value = "/recipe/deleteComment", method = RequestMethod.POST)
    public ResponseEntity<?> deleteComment(@RequestBody commentRecipeReq req) {
        return recipeService.deleteComment(req.getViewerUserId(), req.getRecipeId());
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class collectRecipeReq {
        @NotNull
        private String viewerUserId;
        @NotNull
        private String collectionId;
        @NotNull
        private String recipeId;
    }

    @RequestMapping(value = "/recipe/collectRecipe", method = RequestMethod.POST)
    public ResponseEntity<?> collectRecipe(@RequestBody collectRecipeReq req) {
        return recipeService.collectRecipe(req.getViewerUserId(), req.getCollectionId(), req.getRecipeId());
    }

    // TODO: just for test need to be deleted
    @RequestMapping(value = "/recipe/testPost", method = RequestMethod.POST)
    public ResponseEntity<?> testPostRecipe(HttpServletRequest request,
                                        @RequestPart(value = "uploadPhotos") MultipartFile[] uploadPhotos,
                                        @RequestPart(value = "jsonData") Recipe recipe) {
        return recipeService.testPost(request, uploadPhotos, recipe);
    }

    // TODO: just for test need to be deleted
    @RequestMapping(value = "/recipe/test_privacy", method = RequestMethod.POST)
    public ResponseEntity<?> testRecipePrivacy(@RequestBody Recipe recipe) {
        return recipeService.testPrivacyRecipe(recipe);
    }

    @RequestMapping(value = "/recipe/deleteRecipe", method = RequestMethod.POST)
    public ResponseEntity<?> deleteRecipe(@RequestBody commentRecipeReq req) {
        return recipeService.deleteRecipe(req.getViewerUserId(), req.getRecipeId());
    }
}
