package com.booknook.exception;

import com.booknook.common.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** 全局异常处理。 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public Result<Void> handleBusiness(BusinessException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleValid(MethodArgumentNotValidException e) {
        return Result.fail(400, "参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        e.printStackTrace();
        return Result.fail(500, "系统异常：" + e.getMessage());
    }
}
