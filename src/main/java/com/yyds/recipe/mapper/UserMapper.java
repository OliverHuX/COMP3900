package com.yyds.recipe.mapper;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void saveUser(User user);

    void saveUserAccount(User user);

    void editUser(User user);

    @Select("select * from user where userId = #{userId}")
    User getUserById(@Param(value = "userId") String userId);

    LoginUser getLoginUserInfo(String email);

    @Update("update user_account set password = #{password}, salt = #{salt} where user_id = #{userId}")
    void changePassword(@Param(value = "userId") String userId, @Param(value = "password") String newPassword, @Param(value = "salt") String salt);

    User getUserByEmail(String email);

    @Select("select user_id from recipe.user where email = #{email}")
    String getUserIdByEmail(@Param(value = "email") String email);

    User getUserByUserId(String userId);

    @Select("select count(1) from recipe.user")
    int testSql();
}
