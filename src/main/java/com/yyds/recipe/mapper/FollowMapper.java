package com.yyds.recipe.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FollowMapper {

    void followUser(String followingId, String followId);
}
