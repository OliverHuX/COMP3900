package com.yyds.recipe.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class editPassword {
    private String oldPassword;
    private String newPassword;
    private String userId;
}
