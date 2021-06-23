package com.yyds.recipe.vo;

public interface ErrorCode {

    // Normal error

    int UNKNOWN_ERROR_CODE = 1;
    String UNKNOWN_ERROR_MESSAGE = "unknown error";


    // 701 - 800 service logic error
    int BUSINESS_PARAMETER_ERROR = 701;
    String BUSINESS_PARAMETER_ERROR_MESSAGE = "business parameter error";

    // 600 - 700 user role error

    // 801 - 900 middleware error
    int DATABASE_GENERAL_ERROR = 801;
    String DATABASE_GENERAL_ERROR_MESSAGE = "mysql general error";

    // 901 - 1000
}
