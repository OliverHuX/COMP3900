package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface RecipeMapper {

    void saveRecipe(Recipe recipe);

    void savePhotos(String recipeId, List<String> uploadPhotos);

    void likeRecipe(String userId, String recipeId);

    void unlikeRecipe(String userId, String recipeId);

    Recipe getRecipeById(String recipeId);

    List<Recipe> getRecipeList(List<String> tagList, String searchContent, String creatorId, String recipeId, String userId);

    List<Recipe> getMyRecipeList(String userId);

    List<String> getFileNameListByRecipeId(String recipeId);

    void saveTagRecipe(String recipeId, List<String> tags);

    int getCountBySpecificRate(String recipeId, String userId);

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

    List<String> getTagListByRecipeId(String recipeId);

    void rateRecipe(String recipeId, String userId, Double rate);

    void updateRate(String recipeId, String userId, Double rate);
}
