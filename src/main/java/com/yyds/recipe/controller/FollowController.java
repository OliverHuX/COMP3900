package com.yyds.recipe.controller;

import com.yyds.recipe.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FollowController {

    @Autowired
    private FollowService followService;

    @RequestMapping(value = "/{userId}/follow")
    public ResponseEntity<?> followUser(@PathVariable("userId") String userId, @RequestBody String followId) {

        return followService.followUser(userId, followId);
    }
}
