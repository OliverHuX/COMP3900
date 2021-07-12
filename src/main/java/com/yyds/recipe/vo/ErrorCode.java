package com.yyds.recipe.vo;

public interface ErrorCode {

    // Normal error

    int UNKNOWN_ERROR_CODE = 1;
    String UNKNOWN_ERROR_MESSAGE = "unknown error";

    // 600 - 700 user role error
    int EMAIL_ALREADY_EXISTS_ERROR = 601;
    String EMAIL_ALREADY_EXISTS_ERROR_MESSAGE = "email already exists";

    int EMAIL_REGEX_ERROR = 602;
    String EMAIL_REGEX_ERROR_MESSAGE = "email is not valid";

    int PASSWORD_REGEX_ERROR = 603;
    String PASSWORD_REGEX_ERROR_MESSAGE = "password is not valid";

    int EMAIL_NOT_EXISTS_ERROR = 604;
    String EMAIL_NOT_EXISTS_ERROR_MESSAGE = "email does not exist";

    int PASSWORD_INCORRECT_ERROR = 605;
    String PASSWORD_INCORRECT_ERROR_MESSAGE = "password incorrect or email incorrect";

    int USERID_NOT_FOUND_ERROR = 606;
    String USERID_NOT_FOUND_ERROR_MESSAGE = "user id not found";

    int EMAIL_VERIFY_ERROR = 607;
    String EMAIL_VERIFY_ERROR_MESSAGE = "email verify expired";

    int IMAGE_VERIFY_ERROR = 608;
    String IMAGE_VERIFY_ERROR_MESSAGE = "image verify expired";

    int RECIPEID_NOT_FOUND_ERROR = 609;
    String RECIPEID_NOT_FOUND_MESSAGE = "recipeId can not be found";

    int EMPTY_COMMENT_ERROR = 610;
    String EMPTY_COMMENT_MESSAGE = "comment is empty";

    // 701 - 800 service logic error
    int BUSINESS_PARAMETER_ERROR = 701;
    String BUSINESS_PARAMETER_ERROR_MESSAGE = "business parameter error";

    // 801 - 900 middleware error
    int DATABASE_GENERAL_ERROR = 801;
    String DATABASE_GENERAL_ERROR_MESSAGE = "mysql general error";

    int REDIS_ERROR = 802;
    String REDIS_ERROR_MESSAGE = "redis general error";

    int MAIL_SERVER_ERROR = 803;
    String MAIL_SERVER_ERROR_MESSAGE = "mail server error";

    // 901 - 1000
}
