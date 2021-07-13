package com.yyds.recipe.exception.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS(200, "success"),

    // 600 - 700 user role error
    EMAIL_ALREADY_EXISTS_ERROR(601, "email already exists"),
    EMAIL_REGEX_ERROR(602, "email is not valid"),
    PASSWORD_REGEX_ERROR(603, "password is not valid"),
    EMAIL_NOT_EXISTS_ERROR(604, "email does not exist"),
    PASSWORD_INCORRECT_ERROR(605, "password incorrect or email incorrect"),
    USERID_NOT_FOUND_ERROR(606, "user id not found"),
    EMAIL_VERIFY_ERROR(607, "email verify expired"),
    EMAIL_REGISTERED_BUT_NOT_VERIFIED(608, "email register in 30 minutes but do not verify please check your email"),

    // 701 - 800 service logic error
    BUSINESS_PARAMETER_ERROR(701, "business parameter error"),

    // 801 - 900 middleware error
    DATABASE_GENERAL_ERROR(801, "mysql database general error"),
    REDIS_ERROR(802, "redis general error"),
    MAIL_SERVER_ERROR(803, "mail server error"),


    ERROR(3900, "error");

    public int code;
    public String message;
}


