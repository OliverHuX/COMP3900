package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Comment {
    private String recipeId;
    private String comment;
    private Integer like;
    private String commentUserId;
    private String createTime;
    private String updateTime;
}
