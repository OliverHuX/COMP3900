package com.yyds.recipe.mapper;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void saveUser (User user);

    void saveUserAccount(User user);

    void editUser(User user);

    @Select("select * from user where userId = #{userId}")
    User getUserbyId(@Param(value = "userId") String userId);

    LoginUser getLoginUserInfo(String email);
}
