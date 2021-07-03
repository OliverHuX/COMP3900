package com.yyds.recipe.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ServiceVO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;
    private T data;

    public ServiceVO(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ServiceVO(Integer code, String message, T info) {
        this.code = code;
        this.message = message;
        this.data = info;
    }
}
