package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Recipe {
    private String recipeId;
    private String introduction;
    private String userId;
    // can use enum to create variety type
    private String variety;
    private String createTime;
    private int likes;
    private Boolean isPrivacy;
    private String image;
    private HashMap<String, String> comments;

    public void addComment(String userId, String comment) {
        comments.put(userId, comment);
    }

    public void deleteComment(String userId) {
        comments.remove(userId);
    }
}
