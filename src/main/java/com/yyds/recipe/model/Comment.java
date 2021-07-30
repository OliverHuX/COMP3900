package com.yyds.recipe.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Comment implements Serializable {
    private String recipeId;
    private String creatorId;
    private String content;
    private String createTime;
    private String updateTime;
}
