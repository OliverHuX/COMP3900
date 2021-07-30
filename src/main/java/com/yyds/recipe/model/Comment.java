package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment implements Serializable {
    private String recipeId;
    private String creatorId;
    private String content;
    private String createTime;
    private String updateTime;
}