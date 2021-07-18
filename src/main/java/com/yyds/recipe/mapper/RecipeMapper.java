package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Recipe;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface RecipeMapper {

    void saveRecipe(Recipe recipe);

    void savePhotos(String recipeId, List<String> uploadPhotos);

    void likeRecipe(String userId, String recipeId);

    void unlikeRecipe(String userId, String recipeId);

    Recipe getRecipeById(String recipeId);

    List<Recipe> getRecipeList();

    @Update("update recipe likes set likes = #{likes}, where recipeId = #{recipeId}")
    void updateRecipeLikes(@Param(value = "recipeId") String recipeId, @Param(value = "likes") int likes);

    @Update("update recipe comments set comments = #{comments}, where recipeId = #{recipeId}")
    void updateRecipeComments(@Param(value = "recipeId") String recipeId,
                              @Param(value = "comments") HashMap<String, String> comments);

    @Update("update user subscribe set subscribe = #{recipeId}, where userId = #{userId})")
    void updateSubscribe(@Param(value = "userId") String userId,
                         @Param(value = "recipeId") List<String> recipeId);


    void updatePrivacy(String recipeId, int isPrivacy);

    @Update("update user subscribe set subscribe = #{recipeId}, where userId = #{userId})")
    void deleteSubscribe(@Param(value = "userId") String userId,
                         @Param(value = "recipeId") List<String> recipeId);

}
