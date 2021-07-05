package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Recipe;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeMapper {

    void saveRecipe(Recipe recipe);

    @Select("select * from user where userId = #{userId}")
    User getUserByUserId(String userId);
}
