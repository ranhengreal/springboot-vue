package com.hezheng.authapi.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
//全局异常
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        logger.error("系统异常",e);
        return Result.error(e.getMessage());
    }

    //自定义异常处理
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result error(CustomException e) {
        logger.error("系统异常",e);
        return Result.error(e.getMessage());
    }
}
