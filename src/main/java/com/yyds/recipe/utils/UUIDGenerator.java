package com.yyds.recipe.utils;

import java.util.UUID;

public class UUIDGenerator {
    public static String createUserId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String createRecipeId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
