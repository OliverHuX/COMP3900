package com.yyds.recipe.service;

import org.springframework.http.ResponseEntity;

public interface CollectionService {

    ResponseEntity<?> addCollection(String userId, String collectionName);

    ResponseEntity<?> removeCollection(String userId, String collectionName);

    ResponseEntity<?> changeCollectionName(String userId, String collectionId, String collectionName);

    ResponseEntity<?> addRecipeToCollection(String userId, String collectionId, String recipeId);

    ResponseEntity<?> removeRecipeFromCollection(String userId, String collectionId, String recipeId);

    ResponseEntity<?> getUserCollections(String userId);

    ResponseEntity<?> getCreatorCollections(String viewerUserId, String creatorUserId);

}
