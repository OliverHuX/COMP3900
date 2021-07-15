package com.yyds.recipe.service;

import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.User;
import org.springframework.http.ResponseEntity;

public interface CollectionService {

    ResponseEntity<?> createCollection(User user, String collectionName);

    ResponseEntity<?> changeCollectionName(Collection collection, String collectionName);
}
