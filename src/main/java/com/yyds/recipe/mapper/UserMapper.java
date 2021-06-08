package com.yyds.recipe.mapper;

import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void saveUser (User user);

    @Select("select * from user where email = #{email}")
    User getUser(@Param(value = "email") String email);
}
