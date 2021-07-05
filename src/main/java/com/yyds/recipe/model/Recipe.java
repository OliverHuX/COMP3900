package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.BufferedInputStream;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Recipe {
    private String recipeId;
    @NotNull
    private String introduction;
    // can use enum to create variety type
    private String variety;
    private String createTime;
    private Integer likes;
    private Boolean privacy;
    @NotNull
    private BufferedInputStream image;
    private HashMap<User, String> comments;
    @NotNull
    private User poster;
}
