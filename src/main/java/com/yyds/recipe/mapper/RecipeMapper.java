package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Recipe;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface RecipeMapper {

    void saveRecipe(Recipe recipe);

    void savePhotos(String recipeId, List<String> uploadPhotos);

    void likeRecipe(String userId, String recipeId);

    void unlikeRecipe(String userId, String recipeId);

    Recipe getRecipeById(String recipeId);

    List<Recipe> getRecipeList();

    List<Recipe> getAllRecipeList();

    List<Recipe> getMyRecipeList(String userId);

    List<String> getFileNameListByRecipeId(String recipeId);

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

    @Select("select count(user_id) from recipe.admin, where user_id = #{userId}")
    int isAdmin(@Param(value = "userId") String userId);

    void removeLikeByRecipeId(String recipeId);

    void removePhotoByRecipeId(String recipeId);

    void removeVideoByRecipeId(String recipeId);

    void removeRecipe(String recipeId);
}
