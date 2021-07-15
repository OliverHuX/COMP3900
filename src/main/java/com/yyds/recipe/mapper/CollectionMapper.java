package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;

public interface CollectionMapper {

    @Update("update user_collections set collections = #{collections} where user = #{user}")
    void updateCollections(@Param(value = "user") User user, @Param(value = "collections") HashMap<String,
            Collection> collections);

}
