package com.yyds.recipe.controller;

import com.yyds.recipe.model.User;
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
    public static class createCollection {
        @NotNull
        private User user;
        @NotNull
        private String collectionName;
    }

    @RequestMapping(value = "/createCollection", method = RequestMethod.POST)
    public ResponseEntity<?> createCollection(@RequestBody createCollection req) {
        return collectionService.createCollection(req.getUser(), req.getCollectionName());
    }

}
