package com.yyds.recipe.controller;

import com.yyds.recipe.service.CollectionService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.constraints.NotNull;

public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class addCollectionReq {
        @NotNull
        private String userId;
        @NotNull
        private String collectionName;
    }

    @RequestMapping(value = "/addCollection", method = RequestMethod.POST)
    public ResponseEntity<?> addCollection(@RequestBody addCollectionReq req) {
        return collectionService.addCollection(req.getUserId(), req.getCollectionName());
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class removeCollectionReq {
        @NotNull
        private String userId;
        @NotNull
        private String collectionId;
    }

    @RequestMapping(value = "/removeCollection", method = RequestMethod.POST)
    public ResponseEntity<?> removeCollection(@RequestBody removeCollectionReq req) {
        return collectionService.removeCollection(req.getUserId(), req.getCollectionId());
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CollectionReq {
        @NotNull
        private String userId;
        @NotNull
        private String collectionId;
        @NotNull
        private String collectionName;
    }

    @RequestMapping(value = "/changeCollectionName", method = RequestMethod.POST)
    public ResponseEntity<?> changeCollectionName(@RequestBody CollectionReq req) {
        return collectionService.changeCollectionName(req.getUserId(), req.getCollectionId(),
                                                      req.getCollectionName());
    }


    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class recipeInCollectionReq {
        @NotNull
        private String userId;
        @NotNull
        private String collectionId;
        @NotNull
        private String recipeId;
    }

    @RequestMapping(value = "/addRecipeToCollection", method = RequestMethod.POST)
    public ResponseEntity<?> addRecipeToCollection(@RequestBody recipeInCollectionReq req) {
        return collectionService.addRecipeToCollection(req.getUserId(), req.getCollectionId(),
                                                      req.getRecipeId());
    }

    @RequestMapping(value = "/removeRecipeToCollection", method = RequestMethod.POST)
    public ResponseEntity<?> removeRecipeToCollection(@RequestBody recipeInCollectionReq req) {
        return collectionService.removeRecipeFromCollection(req.getUserId(), req.getCollectionId(),
                                                       req.getRecipeId());
    }

}
