package com.yyds.recipe.exception.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "success"),

    // 600 -
    PARAMETER_ERROR(600, "business parameter error"),
    EMAIL_ALREADY_EXISTS_ERROR(601, "email already exists"),
    EMAIL_REGEX_ERROR(602, "email is not valid"),
    PASSWORD_REGEX_ERROR(603, "password is not valid"),
    EMAIL_OR_PASSWORD_ERROR(604, "email or password incorrect"),
    EMAIL_VERIFY_ERROR(605, "email verify error"),


    // 801 -  middleware error
    DATABASE_GENERAL_ERROR(801, "mysql database general error"),
    REDIS_ERROR(802, "redis general error"),
    MAIL_SERVER_ERROR(803, "mail server error");


    public int code;
    public String message;
}


