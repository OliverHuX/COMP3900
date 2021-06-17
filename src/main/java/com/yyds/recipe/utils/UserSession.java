package com.yyds.recipe.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSession {
    public static final String ATTRIBUTE_ID = "user-login";
    private String userId;
}
