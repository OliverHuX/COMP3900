package com.yyds.recipe.service.impl;

import com.yyds.recipe.exception.response.ResponseCode;
import com.yyds.recipe.mapper.CollectionMapper;
import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.User;
import com.yyds.recipe.service.CollectionService;
import com.yyds.recipe.utils.ResponseUtil;
import com.yyds.recipe.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public ResponseEntity<?> createCollection(User user, String collectionName) {

        if (user.getUserId() == null) {
            return ResponseUtil.getResponse(ResponseCode.USERID_NOT_FOUND_ERROR, null, null);
        }

        if (collectionName == null) {
            return ResponseUtil.getResponse(ResponseCode.BUSINESS_PARAMETER_ERROR, null, null);
        }

        String collectionId = UUIDGenerator.createCollectionId();
        Collection newCollection = new Collection();
        newCollection.setCollectionId(collectionId);
        newCollection.setCollectionName(collectionName);
        newCollection.setCollectorId(user.getUserId());

        HashMap<String, Collection> collections = user.getCollections();
        collections.put(collectionId, newCollection);

        user.setCollections(collections);

        try {
            collectionMapper.updateCollections(user, user.getCollections());
        } catch (Exception e) {
            return ResponseUtil.getResponse(ResponseCode.DATABASE_GENERAL_ERROR, null, null);
        }

        return ResponseUtil.getResponse(ResponseCode.SUCCESS, null, null);
    }
}
