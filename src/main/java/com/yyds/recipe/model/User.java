package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private int userId;

    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    private int gender;
    @NonNull
    private String email;
    private String nickName;
    @NonNull
    private String password;
    private String birthDate;
}
