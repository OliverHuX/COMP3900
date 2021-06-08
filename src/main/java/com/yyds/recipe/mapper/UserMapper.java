package com.yyds.recipe.mapper;

import com.yyds.recipe.model.RegisterUser;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {

    void saveUser (RegisterUser registerUser);

}
