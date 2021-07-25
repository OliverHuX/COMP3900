package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Comment;
import com.yyds.recipe.model.Recipe;
import org.apache.ibatis.annotations.*;
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

    @Update("update recipe.likes set likes = #{likes}, where recipe_id = #{recipeId}")
    void updateRecipeLikes(@Param(value = "recipeId") String recipeId, @Param(value = "likes") int likes);

    @Update("update recipe.comments set comments = #{comments}, where recipe_id = #{recipeId}")
    void updateRecipeComments(@Param(value = "recipeId") String recipeId,
                              @Param(value = "comments") HashMap<String, String> comments);

    void saveComment(Comment comment);

    @Delete("delete from recipe.comment where recipe_id = #{recipeId} and comment_id = #{commentId}")
    void deleteComments(@Param(value = "recipeId") String recipeId,
                        @Param(value = "commentId") Integer commentId);

    @Insert("insert into recipe.subscribe (recipe_id, user_id) values (#{recipeId}, #{userId})")
    void updateSubscribe(@Param(value = "userId") String userId,
                         @Param(value = "recipeId") List<String> recipeId);


    void updatePrivacy(String recipeId, int isPrivacy);

    @Delete("delete from recipe.subscribe where recipe_id = #{recipeId} and user_id = #{userId}")
    void deleteSubscribe(@Param(value = "userId") String userId,
                         @Param(value = "recipeId") List<String> recipeId);

    @Select("select count(user_id) from recipe.admin where user_id = #{userId}")
    int isAdmin(@Param(value = "userId") String userId);

    @Delete("delete from recipe.like where recipe_id = #{recipeId}")
    void removeLikeByRecipeId(String recipeId);

    @Delete("delete from recipe.photo where recipe_id = #{recipeId}")
    void removePhotoByRecipeId(String recipeId);

    @Delete("delete from recipe.video where recipe_id = #{recipeId}")
    void removeVideoByRecipeId(String recipeId);

    @Delete("delete from recipe.comment where recipe_id = #{recipeId}")
    void removeCommentByRecipeId(String recipeId);

    @Delete("delete from recipe.recipe where recipe_id = #{recipeId}")
    void removeRecipe(String recipeId);
}
