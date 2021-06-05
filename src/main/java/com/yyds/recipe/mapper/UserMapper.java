package com.yyds.recipe.mapper;

import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int RegisterUser(User user);
}
