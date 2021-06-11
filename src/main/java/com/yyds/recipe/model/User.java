package com.yyds.recipe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    @Size(min = 0, max = 2)
    private int gender;
    @NotNull
    @Email
    @NotEmpty
    private String email;
    private String nickName;
    @NotNull
    @NotEmpty
    private String password;
    private String birthdate;
    private String createTime;
    private String profilePhoto;
}
