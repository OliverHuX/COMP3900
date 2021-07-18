package com.yyds.recipe.service;

import org.springframework.http.ResponseEntity;

public interface FollowService {
    ResponseEntity<?> followUser(String userId, String followId);
}
