package com.hezheng.authapi.common;

import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private int code;
    public CustomException(int code,String message){
        super(message);
        this.code = code;
    }
    public CustomException(String message){
        super(message);
        this.code = 500;
    }
}
