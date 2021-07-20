package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Collection implements Serializable {

    private String collectionId;
    private String collectionName;
    private List<Recipe> recipes;
    private String collectorId;

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }
}
