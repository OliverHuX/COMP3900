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

import javax.validation.constraints.NotNull;

@RestController
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @RequestMapping(value = "/{userId}/postRecipe", method = RequestMethod.POST)
    public ResponseEntity<?> postRecipe(@PathVariable(value = "userId") String userId,
                                        @RequestPart(value = "uploadPhotos") MultipartFile[] uploadPhotos,
                                        @RequestPart(value = "jsonData") Recipe recipe) {
        return recipeService.postRecipe(userId, uploadPhotos, recipe);
    }

    @RequestMapping(value = "/recipe/like", method = RequestMethod.POST)
    public ResponseEntity<?> likeRecipe(@RequestBody String recipeId) {
        return recipeService.likeRecipe(recipeId);
    }

    @RequestMapping(value = "/recipe/unlike", method = RequestMethod.POST)
    public ResponseEntity<?> unlikeRecipe(@RequestBody String recipeId) {
        return recipeService.unlikeRecipe(recipeId);
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
}
