package com.yyds.recipe.exception.handle;

import com.yyds.recipe.exception.AuthorizationException;
import com.yyds.recipe.vo.ErrorCode;
import com.yyds.recipe.vo.ServiceVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(AuthorizationException.class)
    @ResponseBody
    public ServiceVO<?> authorizationException() {
        return new ServiceVO<>(ErrorCode.USER_AUTHORIZATION_ERROR, ErrorCode.USER_AUTHORIZATION_ERROR_MESSAGE);
    }
}
