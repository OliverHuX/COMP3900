package com.yyds.recipe.mapper;

import com.yyds.recipe.model.LoginUser;
import com.yyds.recipe.model.User;
import org.apache.ibatis.annotations.Insert;
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

    @Select("select password from user_account where userId = #{userId}")
    String getPasswordByUserid(String userId);

    @Insert("update user_account set password = #{newPassword} where userId = #{userId}")
    void changePassword(String userId, String newPassword);
}
