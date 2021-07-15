package com.yyds.recipe.mapper;

import com.yyds.recipe.model.Collection;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;

public interface CollectionMapper {

    @Update("update user_collections set collections = #{collections} where userId = #{userId}")
    void updateCollections(@Param(value = "userId") String userId, @Param(value = "collections") HashMap<String,
            Collection> collections);

//    @Update("update collection_name set collectionName = #{collectionName} where collection = #{collection}")
//    void updateCollectionName(@Param(value = "collection")Collection collection,
//                              @Param(value = "collectionName") String collectionName);

    @Select("select collection from user where userId = #{userId}, collectionId = #{collectionId}")
    Collection getCollectionById(@Param(value = "userId") String userId,
                             @Param(value = "collectionId") String collectionId);
}
