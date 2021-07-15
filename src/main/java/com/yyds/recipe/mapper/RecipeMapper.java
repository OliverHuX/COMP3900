package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface RecipeMapper {

    void saveRecipe(Recipe recipe);

    @Select("select * from user where recipeId = #{recipeId}")
    Recipe getRecipeById(String recipeId);

    @Update("update recipe likes set likes = #{likes}, where recipeId = #{recipeId}")
    void updateRecipeLikes(@Param(value = "recipeId") String recipeId, @Param(value = "likes") int likes);

    @Update("update recipe comments set comments = #{comments}, where recipeId = #{recipeId}")
    void updateRecipeComments(@Param(value = "recipeId") String recipeId,
                              @Param(value = "comments")HashMap<User, String> comments);

    @Update("update user subscribe set subscribe = #{recipeId}, where userId = #{userId})")
    void updateSubscribe(@Param(value = "userId") String userId,
                         @Param(value = "recipeId") List<String> recipeId);

    @Update("update recipe privacy set privacy = #{privacy}, where recipeId = #{recipeId})")
    void updatePrivacy(@Param(value = "recipeId") String recipeId,
                       @Param(value = "privacy") Boolean privacy);
}
