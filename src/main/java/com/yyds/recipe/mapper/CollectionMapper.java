package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Collection;
import com.yyds.recipe.model.Recipe;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionMapper {

    void saveCollection(Collection collection);

    @Update("update collection set recipes =#{recipes} where collectionId =#{collectionId}")
    void updateCollectionRecipes(@Param(value = "collectionId") String collectionId,
                                 @Param(value = "recipes") List<Recipe> recipes);

    @Delete("delete from collection where collectionId =#{collectionId}")
    void removeCollection(@Param(value = "collectionId") String collectionId);

    @Update("update collection_name set collectionName = #{collectionName} where collectionId = #{collectionId}")
    void changeCollectionName(@Param(value = "collection")String collectionId,
                              @Param(value = "collectionName") String collectionName);

    @Select("select * from collection where collectionId = #{collectionId}")
    Collection getCollectionById(@Param(value = "collectionId") String collectionId);

    @Update("update collection_name set collectionName = #{collectionName} where collectionId = #{collectionId}")
    void updateCollectionName(@Param(value = "collection")String collectionId,
                              @Param(value = "collectionName") String collectionName);
}
